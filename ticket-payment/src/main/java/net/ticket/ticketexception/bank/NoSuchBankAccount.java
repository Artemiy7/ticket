package net.ticket.ticketexception.bank;

public class NoSuchBankAccount extends Exception {
    public NoSuchBankAccount(String message) {
        super(message);
    }
}
