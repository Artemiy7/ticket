package net.ticket.client.bank;

import net.ticket.request.payment.PaymentRequest;
import net.ticket.ticketexception.bank.BankServerError;
import net.ticket.ticketexception.bank.NoSuchBankAccount;
import net.ticket.ticketexception.bank.NotEnoughAmountForPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;


@Service
public class BankIntegrationClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(BankIntegrationClient.class);
    private final String bankAccountUri;
    private final String bankPaymentUri;
    private final RestTemplate restTemplate;

    @Autowired
    public BankIntegrationClient(@Value("${service.bank.account.uri}") String bankAccountUri,
                                 @Value("${service.bank.payment.uri}")String bankPaymentUri,
                                 RestTemplate restTemplate) {
        this.bankAccountUri = bankAccountUri;
        this.bankPaymentUri = bankPaymentUri;
        this.restTemplate = restTemplate;
    }

    public void sendConfirmationRequestToBankAccount(long bankAccount) throws NoSuchBankAccount, BankServerError {
       String bankUri = String.format(bankAccountUri, bankAccount);
        try {
            HttpStatus httpStatus = restTemplate.getForEntity(bankUri, HttpStatus.class).getStatusCode();
            if (httpStatus.equals(HttpStatus.OK)) {
                LOGGER.info("Confirmation request is successful " + bankAccount);
            }
        } catch (HttpClientErrorException.NotFound e) {
            LOGGER.error("No such bank account in a bank " + bankAccount);
            throw new NoSuchBankAccount("No such bank account in a bank");
        } catch (HttpServerErrorException.InternalServerError e) {
            LOGGER.error("Error on bank server while confirming bank account " + bankAccount);
            throw new BankServerError("Error on bank server while confirming bank account");
        } catch (HttpStatusCodeException e) {
            LOGGER.error("Error while performing confirmation request to bank " + bankAccount);
            throw new RuntimeException("Error while performing confirmation request to bank " + e.getMessage());
        }
        LOGGER.error("Unknown response while performing confirmation request to " + bankAccount);
        throw new RuntimeException("Unknown response while performing confirmation request");
    }

    public void performPaymentRequestToBank(PaymentRequest paymentRequest) throws NotEnoughAmountForPayment, BankServerError {
        try {
            HttpStatus httpStatus = restTemplate.postForEntity(bankPaymentUri, paymentRequest, HttpStatus.class).getStatusCode();
            if (httpStatus.equals(HttpStatus.OK)) {
                LOGGER.info("Payment request is successful " + paymentRequest.getBankAccount());
            }
        } catch (HttpClientErrorException.NotFound e) {
            LOGGER.error("Not enough amount for payment " + paymentRequest.getBankAccount());
            throw new NotEnoughAmountForPayment("Not enough amount for payment");
        } catch (HttpServerErrorException.InternalServerError e) {
            LOGGER.error("Error on bank server while performing payment " + paymentRequest.getBankAccount());
            throw new BankServerError("Error on bank server while performing payment");
        } catch (HttpStatusCodeException e) {
            LOGGER.error("Error while performing payment request to bank " + paymentRequest.getBankAccount());
            throw new RuntimeException("Error while performing payment request to bank " + e.getMessage());
        }
        LOGGER.error("Unknown response while performing payment to " + paymentRequest.getBankAccount());
        throw new RuntimeException("Unknown response  while performing payment");
    }
}
