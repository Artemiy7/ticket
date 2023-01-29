package net.ticket.service.ticketorder;

import net.ticket.entity.ticketorder.CustomerTicketEntity;
import net.ticket.entity.ticketorder.TicketOrderEntity;
import net.ticket.request.payment.PaymentRequest;
import net.ticket.dto.ticketorder.CustomerTicketDto;
import net.ticket.dto.ticketorder.TicketOrderDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class PaymentRequestService {
    public PaymentRequest buildPaymentRequest(TicketOrderEntity ticketOrderEntity) {
        return PaymentRequest.builder()
                         .bankAccount(ticketOrderEntity.getBankAccount())
                         .ticketOrderId(ticketOrderEntity.getTicketOrderId())
                         .amountSum(addTicketCostForPaymentRequest(ticketOrderEntity.getCustomersEntitySet()))
                         .build();
    }

    private BigDecimal addTicketCostForPaymentRequest(Set<CustomerTicketEntity> customerTicketEntities) {
        BigDecimal resultCost = BigDecimal.ZERO;
        customerTicketEntities.forEach(customerDto -> resultCost.add(customerDto.getAmount()));
        return resultCost;
    }
}
