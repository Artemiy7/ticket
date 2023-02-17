package net.ticket.ticketexception.occasion;

public class NoSuchOccasionSeatException extends RuntimeException {
    public NoSuchOccasionSeatException(String message) {
        super(message);
    }
}
