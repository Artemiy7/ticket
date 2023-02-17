package net.ticket.ticketexception.occasion;

public class OccasionOutdatedException extends RuntimeException {
    public OccasionOutdatedException(String message) {
        super(message);
    }
}
