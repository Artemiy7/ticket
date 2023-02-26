package net.ticket.ticketexception;

import lombok.Getter;

import java.util.List;

@Getter
public class DeserializationException extends RuntimeException {
    private List<String> resultValidations;
    private String message;
    public DeserializationException(List<String> resultValidations, String message) {
        this.resultValidations = resultValidations;
        this.message = message;
    }
}
