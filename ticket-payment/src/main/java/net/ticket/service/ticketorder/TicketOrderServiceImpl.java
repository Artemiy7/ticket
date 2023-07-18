package net.ticket.service.ticketorder;

import net.ticket.domain.entity.occasion.OccasionSeatEntity;
import net.ticket.domain.entity.ticketorder.TicketOrderEntity;
import net.ticket.repository.occasion.OccasionRepository;
import net.ticket.repository.occasion.OccasionSeatRepository;
import net.ticket.repository.ticketorder.TicketOrderRepository;
import net.ticket.dto.ticketorder.CustomerTicketDto;
import net.ticket.dto.ticketorder.TicketOrderDto;
import net.ticket.domain.entity.occasion.OccasionEntity;
import net.ticket.response.currencyexchange.CurrencyExchangeResponse;
import net.ticket.client.bank.BankSimulatorClient;
import net.ticket.client.currencyexchange.CurrencyExchangeClient;
import net.ticket.client.pdfgenerator.PdfGeneratorClient;
import net.ticket.ticketexception.bank.InvalidBankAccount;
import net.ticket.ticketexception.occasion.NoSuchOccasionException;
import net.ticket.ticketexception.occasion.NoSuchOccasionSeatException;
import net.ticket.ticketexception.ticketorder.NoSuchTicketOrderEntityException;
import net.ticket.ticketexception.occasion.OccasionSeatIsBookedException;
import org.springframework.stereotype.Service;
import net.ticket.ticketexception.bank.BankServerError;
import net.ticket.ticketexception.bank.NoSuchBankAccount;
import net.ticket.ticketexception.bank.NotEnoughAmountForPayment;
import net.ticket.transformer.ticketorder.TicketOrderDtoToTicketOrderEntityTransformer;
import net.ticket.transformer.ticketorder.TicketOrderEntityToTicketOrderDtoTransformer;
import net.ticket.util.ValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.Optional;

@Service
public class TicketOrderServiceImpl implements TicketOrderService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TicketOrderServiceImpl.class);
    private final String defaultCurrency;
    private final TicketOrderRepository ticketOrderRepository;
    private final OccasionRepository occasionRepository;
    private final OccasionSeatRepository occasionSeatRepository;
    private final TicketOrderEntityToTicketOrderDtoTransformer ticketOrderEntityToTicketOrderDtoTransformer;
    private final TicketOrderDtoToTicketOrderEntityTransformer ticketOrderDtoToTicketOrderEntityTransformer;
    private final PdfGeneratorClient pdfGeneratorClient;
    private final BankSimulatorClient bankSimulatorClient;
    private final CurrencyExchangeClient currencyExchangeClient;
    private final PaymentRequestService paymentRequestService;
    private final ValidatorUtils validatorUtils;


    public TicketOrderServiceImpl(@Value("${service.default-currency}") String defaultCurrency,
                              TicketOrderRepository ticketOrderRepository,
                              OccasionRepository occasionRepository,
                              OccasionSeatRepository occasionSeatRepository,
                              TicketOrderEntityToTicketOrderDtoTransformer ticketOrderEntityToTicketOrderDtoTransformer,
                              TicketOrderDtoToTicketOrderEntityTransformer ticketOrderDtoToTicketOrderEntityTransformer,
                              BankSimulatorClient bankSimulatorClient,
                              CurrencyExchangeClient currencyExchangeClient,
                              PdfGeneratorClient pdfGeneratorClient,
                              PaymentRequestService paymentRequestService,
                              ValidatorUtils validatorUtils) {
        this.defaultCurrency = defaultCurrency;
        this.ticketOrderRepository = ticketOrderRepository;
        this.occasionRepository = occasionRepository;
        this.occasionSeatRepository = occasionSeatRepository;
        this.ticketOrderEntityToTicketOrderDtoTransformer = ticketOrderEntityToTicketOrderDtoTransformer;
        this.ticketOrderDtoToTicketOrderEntityTransformer = ticketOrderDtoToTicketOrderEntityTransformer;
        this.bankSimulatorClient = bankSimulatorClient;
        this.currencyExchangeClient = currencyExchangeClient;
        this.pdfGeneratorClient = pdfGeneratorClient;
        this.paymentRequestService = paymentRequestService;
        this.validatorUtils = validatorUtils;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Optional<Long> createTicket(TicketOrderDto ticketOrderDto) throws NoResultException, NoSuchOccasionException,
            OccasionSeatIsBookedException, NoSuchBankAccount, BankServerError, NotEnoughAmountForPayment, NoSuchOccasionSeatException, InvalidBankAccount {
        Optional<OccasionEntity> occasionEntityOptional = occasionRepository.findOccasionByNameAndDateAndAddress(ticketOrderDto);
        if (occasionEntityOptional.isEmpty())
            return Optional.empty();

        for(CustomerTicketDto customerTicketDto : ticketOrderDto.getCustomerTicketDto()) {
            OccasionSeatEntity occasionSeatEntity =
                    occasionSeatRepository.findOccasionSeatsByOccasionIdAndSeat(occasionEntityOptional.get(),
                            customerTicketDto).orElseThrow(() -> new NoSuchOccasionSeatException("OccasionSeat" + customerTicketDto.getSeat() +
                            " is not exist in occasion " + customerTicketDto.getTicketOrderDto().getOccasionName()));
            if (occasionSeatEntity.isBooked())
                throw new OccasionSeatIsBookedException("OccasionSeat is booked " + occasionSeatEntity.getOccasionSeatId());
            customerTicketDto.setOccasionSeat(occasionSeatEntity);
        }
        bankSimulatorClient.sendConfirmationRequestToBankAccount(ticketOrderDto.getBankAccount());

        TicketOrderEntity ticketOrderEntity = ticketOrderDtoToTicketOrderEntityTransformer.transform(ticketOrderDto);
        ticketOrderRepository.saveTicketOrder(ticketOrderEntity);
        LOGGER.info("TicketOrder saved successfully, bankAccount: " + ticketOrderEntity.getBankAccount() +
                " ticketOrderId: " + ticketOrderEntity.getTicketOrderId() +
                " ticketType " + ticketOrderEntity.getTicketType());
        ticketOrderDto.getCustomerTicketDto()
                .forEach(customerDto -> {
                    if (!ticketOrderDto.getCurrency().equals(defaultCurrency)) {
                        CurrencyExchangeResponse currencyExchangeResponse =
                                currencyExchangeClient.sendRequestToCurrencyExchange(ticketOrderDto.getCurrency(),
                                        defaultCurrency, customerDto.getAmount());
                        customerDto.setAmount(currencyExchangeResponse.getAmount());
                    }
                    occasionSeatRepository.updateOccasionSeatSetIsBooked(occasionEntityOptional.get(), customerDto);
                    LOGGER.info("Successfully updated OccasionSeat, seat: " + customerDto.getOccasionSeat().getOccasionEntity().getOccasionId() +
                            " OccasionId " + customerDto.getOccasionSeat().getOccasionSeatId());
                });

        if (bankSimulatorClient.performPaymentRequestToBank(paymentRequestService.buildPaymentRequest(ticketOrderEntity)))
            ticketOrderEntity.setPaid(true);

        LOGGER.info("TicketOrder created successfully, bankAccount: " + ticketOrderEntity.getBankAccount() +
                " ticketOrderId: " + ticketOrderEntity.getTicketOrderId() +
                " ticketType " + ticketOrderEntity.getTicketType() +
                " ticketOrderId " + ticketOrderEntity.getTicketOrderId());
        return Optional.of(ticketOrderEntity.getTicketOrderId());
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<byte[]> generatePdf(long ticketOrderId) {
        TicketOrderEntity ticketOrderEntity = ticketOrderRepository.findTicketOrder(ticketOrderId)
                                                                   .orElseThrow(() -> new NoSuchTicketOrderEntityException("No such TicketOrder " + ticketOrderId));
        TicketOrderDto ticketOrderDto = ticketOrderEntityToTicketOrderDtoTransformer.transform(ticketOrderEntity);
        validatorUtils.validationBeforeDeserialization(ticketOrderDto);
        ticketOrderDto.getCustomerTicketDto().forEach(validatorUtils::validationBeforeDeserialization);
        return pdfGeneratorClient.performRequestToPdfGeneratorService(ticketOrderDto);
    }
}
