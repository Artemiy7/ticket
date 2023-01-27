package net.ticket.ticketexception.bank;

public class BankServerError extends Exception {
    public BankServerError(String message) {
        super(message);
    }
}
