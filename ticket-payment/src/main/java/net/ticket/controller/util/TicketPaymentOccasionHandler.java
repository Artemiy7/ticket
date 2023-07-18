package net.ticket.controller.util;

import net.ticket.controller.occasion.OccasionController;
import net.ticket.controller.ticketorder.TicketOrderController;
import net.ticket.response.error.ErrorResponse;
import net.ticket.response.error.SerializationErrorResponse;
import net.ticket.ticketexception.DeserializationException;
import net.ticket.ticketexception.SerializationException;
import net.ticket.ticketexception.occasion.NoSuchOccasionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice(basePackageClasses = {OccasionController.class, TicketOrderController.class})
public class TicketPaymentOccasionHandler {

    @ExceptionHandler({NoSuchOccasionException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchOccasionException(NoSuchOccasionException noSuchOccasionException, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Occasion-message", noSuchOccasionException.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                                                 .httpStatus(HttpStatus.NOT_FOUND)
                                                 .message(noSuchOccasionException.getMessage())
                                                 .localDateTime(LocalDateTime.now())
                                                 .path(request.getDescription(false))
                                                 .build(), headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({SerializationException.class})
    public ResponseEntity<ErrorResponse> handleSerializationError(SerializationException serializationException, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Occasion-message", serializationException.getMessage());
        return new ResponseEntity<>(SerializationErrorResponse.builder()
                                                      .httpStatus(HttpStatus.BAD_REQUEST)
                                                      .message(serializationException.getMessage())
                                                      .localDateTime(LocalDateTime.now())
                                                      .path(request.getDescription(false))
                                                      .resultValidations(serializationException.getResultValidations())
                                                      .build(), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DeserializationException.class})
    public ResponseEntity<ErrorResponse> handleDeserializationException(DeserializationException deserializationException, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Occasion-message", deserializationException.getMessage());
        return new ResponseEntity<>(SerializationErrorResponse.builder()
                                                      .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                                                      .message(deserializationException.getMessage())
                                                      .localDateTime(LocalDateTime.now())
                                                      .path(request.getDescription(false))
                                                      .resultValidations(deserializationException.getResultValidations())
                                                      .build(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
