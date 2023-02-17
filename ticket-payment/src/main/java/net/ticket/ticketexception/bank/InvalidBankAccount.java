package net.ticket.ticketexception.bank;

public class InvalidBankAccount extends RuntimeException {
    public InvalidBankAccount(String message) {
        super(message);
    }
}
