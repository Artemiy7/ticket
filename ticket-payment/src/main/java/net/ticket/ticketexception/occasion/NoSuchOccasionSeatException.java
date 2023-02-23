package net.ticket.ticketexception.occasion;

import lombok.Getter;

@Getter
public class NoSuchOccasionSeatException extends OccasionException {
    private String message;
    public NoSuchOccasionSeatException(String message) {
        super(message);
        this.message = message;
    }
}
