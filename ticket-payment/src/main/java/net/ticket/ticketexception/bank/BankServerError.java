package net.ticket.ticketexception.bank;

public class BankServerError extends RuntimeException {
    public BankServerError(String message) {
        super(message);
    }
}
