package net.ticket.client.bank;

import lombok.SneakyThrows;
import net.ticket.config.web.client.BankSimulatorClientConfig;
import net.ticket.request.payment.PaymentRequest;
import net.ticket.ticketexception.bank.BankServerError;
import net.ticket.ticketexception.bank.InvalidBankAccount;
import net.ticket.ticketexception.bank.NoSuchBankAccount;
import net.ticket.ticketexception.bank.NotEnoughAmountForPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;


@Service
public class BankSimulatorClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(BankSimulatorClient.class);
    private final BankSimulatorClientConfig bankSimulatorClientConfig;
    private final RestTemplate restTemplate;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public BankSimulatorClient(BankSimulatorClientConfig bankSimulatorClientConfig,
                               RestTemplate restTemplate,
                               CircuitBreakerFactory circuitBreakerFactory) {
        this.bankSimulatorClientConfig = bankSimulatorClientConfig;
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public void sendConfirmationRequestToBankAccount(long bankAccount) throws NoSuchBankAccount, BankServerError, InvalidBankAccount {
       String bankUri = String.format(bankSimulatorClientConfig.getAccountUrl(), bankAccount);
       boolean response = circuitBreakerFactory
               .create("bank-simulator")
               .run(() -> restTemplate.getForEntity(bankUri, Boolean.class).getBody(),
                    throwable -> confirmationRequestFallback(throwable, bankAccount));
       if (response)
           LOGGER.info("Confirmation request is successful " + bankAccount);
    }

    public boolean performPaymentRequestToBank(PaymentRequest paymentRequest) throws NotEnoughAmountForPayment, BankServerError {
        boolean response = circuitBreakerFactory
                .create("bank-simulator")
                .run(() -> restTemplate.postForEntity(bankSimulatorClientConfig.getPaymentUrl(), paymentRequest, Boolean.class).getBody(),
                        throwable -> performPaymentRequestToBankFallback(throwable, paymentRequest));
        if (response) {
            LOGGER.info("Payment request is successful " + paymentRequest.getBankAccount());
            return true;
        }
        throw new RuntimeException();
    }

    @SneakyThrows
    private boolean confirmationRequestFallback(Throwable throwable, long bankAccount) {
        if (throwable instanceof HttpClientErrorException.NotFound) {
            LOGGER.error("No such bank account in a bank " + bankAccount);
            throw new NoSuchBankAccount("No such bank account in a bank");
        } else if (throwable instanceof HttpServerErrorException.InternalServerError) {
            LOGGER.error("Error on bank server while confirming bank account " + bankAccount);
            throw new BankServerError("Error on bank server while confirming bank account");
        } else if (throwable instanceof HttpClientErrorException.NotAcceptable) {
            LOGGER.error("Bank card is outdated " + bankAccount);
            throw new InvalidBankAccount("Bank card is outdated " + bankAccount);
        } else if (throwable instanceof HttpStatusCodeException) {
            LOGGER.error("Error while performing confirmation request to bank " + bankAccount);
            throw new RuntimeException("Error while performing confirmation request to bank " +
                    bankAccount + " " + throwable.getMessage());
        }
        throw new RuntimeException(throwable);
    }

    @SneakyThrows
    private boolean performPaymentRequestToBankFallback(Throwable throwable, PaymentRequest paymentRequest) {
        if (throwable instanceof HttpClientErrorException.NotFound) {
            LOGGER.error("No such bank account in a bank " + paymentRequest.getBankAccount());
            throw new NoSuchBankAccount("No such bank account in a bank");
        } else if (throwable instanceof  HttpClientErrorException.NotAcceptable) {
            LOGGER.error("Not enough amount for payment " + paymentRequest.getBankAccount());
            throw new NotEnoughAmountForPayment("Not enough amount for payment");
        } else if (throwable instanceof HttpServerErrorException.InternalServerError) {
            LOGGER.error("Error on bank server while performing payment " + paymentRequest.getBankAccount());
            throw new BankServerError("Error on bank server while performing payment");
        } else if (throwable instanceof HttpStatusCodeException) {
            LOGGER.error("Error while performing payment request to bank " + paymentRequest.getBankAccount());
            throw new RuntimeException("Error while performing payment request to bank " +
                    paymentRequest.getBankAccount() + " " + throwable.getMessage());
        }
        throw new RuntimeException(throwable);
    }
}
