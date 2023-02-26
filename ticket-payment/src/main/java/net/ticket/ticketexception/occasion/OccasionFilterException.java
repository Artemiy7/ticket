package net.ticket.ticketexception.occasion;

public class OccasionFilterException extends OccasionException {
    private String message;
    public OccasionFilterException(String message) {
        super(message);
        this.message = message;
    }
}
