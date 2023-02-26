package net.ticket.response.error;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class SerializationErrorResponse extends ErrorResponse {
    private List<String> resultValidations;
}
