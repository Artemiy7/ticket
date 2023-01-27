package net.ticket.service.ticketorder;

import net.ticket.request.payment.PaymentRequest;
import net.ticket.dto.ticketorder.CustomerTicketDto;
import net.ticket.dto.ticketorder.TicketOrderDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class PaymentRequestService {
    public PaymentRequest buildPaymentRequest(TicketOrderDto ticketOrderDto) {
        return PaymentRequest.builder()
                         .bankAccount(ticketOrderDto.getBankAccount())
                         .amountSum(addTicketCostForPaymentRequest(ticketOrderDto.getCustomerTicketDto()))
                         .build();
    }

    private BigDecimal addTicketCostForPaymentRequest(Set<CustomerTicketDto> customerTicketDtoSet) {
        BigDecimal resultCost = BigDecimal.ZERO;
        customerTicketDtoSet.forEach(customerDto -> resultCost.add(customerDto.getAmount()));
        return resultCost;
    }
}
