package net.ticket.ticketexception.occasion;

import lombok.Getter;

@Getter
public class CorruptedOccasionSeatException extends OccasionException {
    private String message;
    public CorruptedOccasionSeatException(String message) {
        super(message);
        this.message = message;
    }
}
