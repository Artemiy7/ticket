package net.ticket.ticketexception.occasion;

import lombok.Getter;

@Getter
public class CorruptedOccasionException extends OccasionException {
    private String message;
    public CorruptedOccasionException(String message) {
        super(message);
        this.message = message;
    }
}
