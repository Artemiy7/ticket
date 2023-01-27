package net.ticket.ticketexception.occasion;

public class NoSuchTicketOrderEntityException extends Exception {
    public NoSuchTicketOrderEntityException(String message) {
        super(message);
    }
}