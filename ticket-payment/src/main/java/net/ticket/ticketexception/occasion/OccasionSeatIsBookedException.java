package net.ticket.ticketexception.occasion;

import lombok.Getter;

@Getter
public class OccasionSeatIsBookedException extends OccasionException {
    private String message;
    public OccasionSeatIsBookedException(String message) {
        super(message);
        this.message = message;
    }
}
