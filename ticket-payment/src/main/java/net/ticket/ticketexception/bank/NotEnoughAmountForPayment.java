package net.ticket.ticketexception.bank;

public class NotEnoughAmountForPayment extends RuntimeException {
    public NotEnoughAmountForPayment(String message) {
        super(message);
    }
}