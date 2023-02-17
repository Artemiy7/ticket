package net.ticket.ticketexception.occasion;

public class OccasionSeatIsBookedException extends RuntimeException {
    public OccasionSeatIsBookedException(String message) {
        super(message);
    }
}
