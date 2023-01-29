package net.ticket.service.ticketorder;

import net.ticket.entity.occasion.OccasionSeatEntity;
import net.ticket.entity.ticketorder.TicketOrderEntity;
import net.ticket.repository.occasion.OccasionRepository;
import net.ticket.repository.occasion.OccasionSeatRepository;
import net.ticket.repository.ticketorder.TicketOrderRepository;
import net.ticket.dto.ticketorder.CustomerTicketDto;
import net.ticket.dto.ticketorder.TicketOrderDto;
import net.ticket.entity.occasion.OccasionEntity;
import net.ticket.response.currencyexchange.CurrencyExchangeResponse;
import net.ticket.client.bank.BankIntegrationClient;
import net.ticket.client.currencyexchange.CurrencyExchangeIntegrationClient;
import net.ticket.client.pdfgenerator.PdfGeneratorIntegrationClient;
import net.ticket.ticketexception.bank.InvalidBankAccount;
import net.ticket.ticketexception.occasion.NoSuchOccasionException;
import net.ticket.ticketexception.occasion.NoSuchOccasionSeatException;
import net.ticket.ticketexception.occasion.NoSuchTicketOrderEntityException;
import net.ticket.ticketexception.occasion.OccasionSeatIsBookedException;
import net.ticket.ticketexception.bank.BankServerError;
import net.ticket.ticketexception.bank.NoSuchBankAccount;
import net.ticket.ticketexception.bank.NotEnoughAmountForPayment;
import net.ticket.transformers.TicketOrderDtoToTicketOrderEntityTransformer;
import net.ticket.transformers.TicketOrderEntityToTicketOrderDtoTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.NoResultException;
import java.util.Optional;
@Component
public class TicketOrderService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TicketOrderService.class);
    private final String defaultCurrency;
    private final TicketOrderRepository ticketOrderRepository;
    private final OccasionRepository occasionRepository;
    private final OccasionSeatRepository occasionSeatRepository;
    private final TicketOrderEntityToTicketOrderDtoTransformer ticketOrderEntityToTicketOrderDtoTransformer;
    private final TicketOrderDtoToTicketOrderEntityTransformer ticketOrderDtoToTicketOrderEntityTransformer;
    private final PdfGeneratorIntegrationClient pdfGeneratorIntegrationClient;
    private final BankIntegrationClient bankIntegrationClient;
    private final CurrencyExchangeIntegrationClient currencyExchangeIntegrationClient;
    private final PaymentRequestService paymentRequestService;
    @Autowired
    public TicketOrderService(@Value("${service.default-currency}") String defaultCurrency,
                              TicketOrderRepository ticketOrderRepository,
                              OccasionRepository occasionRepository,
                              OccasionSeatRepository occasionSeatRepository,
                              TicketOrderEntityToTicketOrderDtoTransformer ticketOrderEntityToTicketOrderDtoTransformer,
                              TicketOrderDtoToTicketOrderEntityTransformer ticketOrderDtoToTicketOrderEntityTransformer,
                              BankIntegrationClient bankIntegrationClient,
                              CurrencyExchangeIntegrationClient currencyExchangeIntegrationClient,
                              PdfGeneratorIntegrationClient pdfGeneratorIntegrationClient,
                              PaymentRequestService paymentRequestService) {
        this.defaultCurrency = defaultCurrency;
        this.ticketOrderRepository = ticketOrderRepository;
        this.occasionRepository = occasionRepository;
        this.occasionSeatRepository = occasionSeatRepository;
        this.ticketOrderEntityToTicketOrderDtoTransformer = ticketOrderEntityToTicketOrderDtoTransformer;
        this.ticketOrderDtoToTicketOrderEntityTransformer = ticketOrderDtoToTicketOrderEntityTransformer;
        this.bankIntegrationClient = bankIntegrationClient;
        this.currencyExchangeIntegrationClient = currencyExchangeIntegrationClient;
        this.pdfGeneratorIntegrationClient = pdfGeneratorIntegrationClient;
        this.paymentRequestService = paymentRequestService;
    }

    @Transactional(rollbackFor = Exception.class)
    public long createTicket(TicketOrderDto ticketOrderDto) throws NoResultException, NoSuchOccasionException,
            OccasionSeatIsBookedException, NoSuchBankAccount, BankServerError, NotEnoughAmountForPayment, NoSuchOccasionSeatException, InvalidBankAccount {
        Optional<OccasionEntity> occasionEntityOptional = occasionRepository.findOccasionByNameAndDate(ticketOrderDto);
        if (occasionEntityOptional.isEmpty())
            throw new NoSuchOccasionException("No such Occasion " + ticketOrderDto.getOccasionName());

        for(CustomerTicketDto customerTicketDto : ticketOrderDto.getCustomerTicketDto()) {
            OccasionSeatEntity occasionSeatEntity =
                    occasionSeatRepository.findOccasionSeats(occasionEntityOptional.get(),
                            customerTicketDto).orElseThrow(() -> new NoSuchOccasionSeatException("OccasionSeat" + customerTicketDto.getSeat() +
                            " is not exist in occasion " + customerTicketDto.getTicketOrderDto().getOccasionName()));
            if (occasionSeatEntity.isBooked())
                throw new OccasionSeatIsBookedException("OccasionSeat is booked " + occasionSeatEntity.getOccasionSeatId());
            customerTicketDto.setOccasionSeat(occasionSeatEntity);
        }
        bankIntegrationClient.sendConfirmationRequestToBankAccount(ticketOrderDto.getBankAccount());

        TicketOrderEntity ticketOrderEntity = ticketOrderDtoToTicketOrderEntityTransformer.transform(ticketOrderDto);
        ticketOrderRepository.saveTicketOrder(ticketOrderEntity);
        LOGGER.info("TicketOrder saved successfully, bankAccount: " + ticketOrderEntity.getBankAccount() +
                " ticketOrderId: " + ticketOrderEntity.getTicketOrderId() +
                " ticketType " + ticketOrderEntity.getTicketType());
        ticketOrderDto.getCustomerTicketDto()
                .forEach(customerDto -> {
                    if (!ticketOrderDto.getCurrency().equals(defaultCurrency)) {
                        CurrencyExchangeResponse currencyExchangeResponse =
                                currencyExchangeIntegrationClient.sendRequestToCurrencyExchange(ticketOrderDto.getCurrency(),
                                        defaultCurrency, customerDto.getAmount());
                        customerDto.setAmount(currencyExchangeResponse.getAmount());
                    }
                    occasionSeatRepository.updateOccasionSeatIsBooked(occasionEntityOptional.get(), customerDto);
                    LOGGER.info("Successfully updated OccasionSeat, seat: " + customerDto.getOccasionSeat().getOccasionEntity().getOccasionId() +
                            " OccasionId " + customerDto.getOccasionSeat().getOccasionSeatId());
                });

        if (bankIntegrationClient.performPaymentRequestToBank(paymentRequestService.buildPaymentRequest(ticketOrderEntity)))
            ticketOrderEntity.setPaid(true);

        LOGGER.info("TicketOrder created successfully, bankAccount: " + ticketOrderEntity.getBankAccount() +
                " ticketOrderId: " + ticketOrderEntity.getTicketOrderId() +
                " ticketType " + ticketOrderEntity.getTicketType() +
                " ticketOrderId " + ticketOrderEntity.getTicketOrderId());
        return ticketOrderEntity.getTicketOrderId();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> generatePdf(long ticketOrderId) throws NoSuchTicketOrderEntityException, NullPointerException, HttpServerErrorException {
        TicketOrderEntity ticketOrderEntity = ticketOrderRepository.findTicketOrder(ticketOrderId)
                                                                   .orElseThrow(() -> new NoSuchTicketOrderEntityException("No such TicketOrder " + ticketOrderId));
        TicketOrderDto ticketOrderDto = ticketOrderEntityToTicketOrderDtoTransformer.transform(ticketOrderEntity);
        return pdfGeneratorIntegrationClient.performRequestToPdfGeneratorService(ticketOrderDto);
    }
}