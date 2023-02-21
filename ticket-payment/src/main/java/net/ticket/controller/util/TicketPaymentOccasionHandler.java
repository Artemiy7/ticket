package net.ticket.controller.util;

import net.ticket.controller.occasion.OccasionController;
import net.ticket.controller.ticketorder.TicketOrderRestController;
import net.ticket.response.error.ErrorResponse;
import net.ticket.ticketexception.occasion.NoSuchOccasionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice(basePackageClasses = {OccasionController.class, TicketOrderRestController.class})
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
                                                 .build(), HttpStatus.NOT_FOUND);
    }
}
