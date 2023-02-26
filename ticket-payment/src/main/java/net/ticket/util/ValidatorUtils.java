package net.ticket.util;

import net.ticket.ticketexception.DeserializationException;
import net.ticket.ticketexception.SerializationException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidatorUtils {
    private final Validator validator;

    public ValidatorUtils(Validator validator) {
        this.validator = validator;
    }

    public <T> void validationAfterSerialization(T req) {
        if (req != null) {
            Set<ConstraintViolation<T>> result = validator.validate(req);
            if (!result.isEmpty()) {
                List<String> resultValidations = result.stream()
                        .map(constraintViolation -> constraintViolation.getPropertyPath().toString() + " " + constraintViolation.getMessage())
                        .collect(Collectors.toList());
                throw new SerializationException(resultValidations, "Serialization Exception");
            }
        }
    }

    public <T> void validationBeforeDeserialization(T req) {
        if (req != null) {
            Set<ConstraintViolation<T>> result = validator.validate(req);
            if (!result.isEmpty()) {
                List<String> resultValidations = result.stream()
                        .map(constraintViolation -> constraintViolation.getPropertyPath().toString() + " " + constraintViolation.getMessage())
                        .collect(Collectors.toList());
                throw new DeserializationException(resultValidations, "Deserialization Exception");
            }
        }
    }
}
