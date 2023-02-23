package net.ticket.ticketexception.occasion;

import lombok.Getter;

@Getter
public class NoSuchOccasionException extends OccasionException {
    private String message;
    public NoSuchOccasionException(String message) {
        super(message);
        this.message = message;
    }
}
