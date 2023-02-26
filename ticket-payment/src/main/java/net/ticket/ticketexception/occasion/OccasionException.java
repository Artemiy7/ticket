package net.ticket.ticketexception.occasion;

import lombok.Getter;

@Getter
public class OccasionException extends RuntimeException {
    private String message;
    public OccasionException(String message) {
        super(message);
        this.message = message;
    }
}
