package net.ticket.ticketexception.occasion;

public class NoSuchOccasionException extends RuntimeException {
    public NoSuchOccasionException(String message) {
        super(message);
    }
}
