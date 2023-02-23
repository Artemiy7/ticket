package net.ticket.ticketexception.occasion;

import lombok.Getter;



@Getter
public class OccasionOutdatedException extends OccasionException {
    private String message;
    public OccasionOutdatedException(String message) {
        super(message);
        this.message = message;
    }
}
