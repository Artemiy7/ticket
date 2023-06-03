package net.ticket.service.ticketorder;

import net.ticket.dto.ticketorder.TicketOrderDto;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface TicketOrderService {
    Optional<Long> createTicket(TicketOrderDto ticketOrderDto);

    ResponseEntity<byte[]> generatePdf(long ticketOrderId);
}
