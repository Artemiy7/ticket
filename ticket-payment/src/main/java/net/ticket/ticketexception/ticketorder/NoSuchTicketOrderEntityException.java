package net.ticket.ticketexception.ticketorder;

import lombok.Getter;

@Getter
public class NoSuchTicketOrderEntityException extends RuntimeException {
    private String message;
    public NoSuchTicketOrderEntityException(String message) {
        super(message);
        this.message = message;
    }
}