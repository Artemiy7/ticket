package net.ticket.service.ticketorder;

import net.ticket.entity.ticketorder.TicketOrderEntity;
import net.ticket.request.payment.PaymentRequest;

public interface PaymentRequestService {
    PaymentRequest buildPaymentRequest(TicketOrderEntity ticketOrderEntity);
}
