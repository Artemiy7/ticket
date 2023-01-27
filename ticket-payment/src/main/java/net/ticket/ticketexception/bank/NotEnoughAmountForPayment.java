package net.ticket.ticketexception.bank;

public class NotEnoughAmountForPayment extends Exception {
    public NotEnoughAmountForPayment(String message) {
        super(message);
    }
}