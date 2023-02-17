package net.ticket.ticketexception.bank;

public class NoSuchBankAccount extends RuntimeException {
    public NoSuchBankAccount(String message) {
        super(message);
    }
}
