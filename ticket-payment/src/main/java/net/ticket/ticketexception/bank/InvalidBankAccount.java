package net.ticket.ticketexception.bank;

public class InvalidBankAccount extends Exception {
    public InvalidBankAccount(String message) {
        super(message);
    }
}
