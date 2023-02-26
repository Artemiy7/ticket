package net.ticket.ticketexception;

import lombok.Getter;

import java.util.List;

@Getter
public class SerializationException extends RuntimeException {
    private List<String> resultValidations;
    private String message;
    public SerializationException(List<String> resultValidations, String message) {
        this.resultValidations = resultValidations;
        this.message = message;
    }
}
