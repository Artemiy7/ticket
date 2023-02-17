package net.ticket.ticketexception.occasion;

public class NoSuchTicketOrderEntityException extends RuntimeException {
    public NoSuchTicketOrderEntityException(String message) {
        super(message);
    }
}