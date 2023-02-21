package net.ticket.controller.occasion.util;

import net.ticket.controller.occasion.OccasionController;
import net.ticket.response.error.ErrorResponse;
import net.ticket.ticketexception.occasion.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice(basePackageClasses = {OccasionController.class})
public class OccasionExceptionHandler {

    @ExceptionHandler({CorruptedOccasionException.class})
    public ResponseEntity<ErrorResponse> handleCorruptedOccasionException(CorruptedOccasionException corruptedOccasionException,  WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Occasion-Error-Message", corruptedOccasionException.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                                                 .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                                                 .message(corruptedOccasionException.getMessage())
                                                 .localDateTime(LocalDateTime.now())
                                                 .path(request.getDescription(false))
                                                 .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({CorruptedOccasionSeatException.class})
    public ResponseEntity<ErrorResponse> handleCorruptedOccasionSeatException(CorruptedOccasionSeatException corruptedOccasionSeatException,  WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Occasion-Seat-Error-Message", corruptedOccasionSeatException.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                                                 .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                                                 .message(corruptedOccasionSeatException.getMessage())
                                                 .localDateTime(LocalDateTime.now())
                                                 .path(request.getDescription(false))
                                                 .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({OccasionOutdatedException.class})
    public ResponseEntity<ErrorResponse> handleOccasionOutdatedException(OccasionOutdatedException occasionOutdatedException,  WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Occasion-Error-Message", occasionOutdatedException.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                                                 .httpStatus(HttpStatus.BAD_REQUEST)
                                                 .message(occasionOutdatedException.getMessage())
                                                 .localDateTime(LocalDateTime.now())
                                                 .path(request.getDescription(false))
                                                 .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({OccasionFilterException.class})
    public ResponseEntity<ErrorResponse> handleOccasionFilterException(OccasionFilterException occasionFilterException,  WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Occasion-Filter-Error-Message", occasionFilterException.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                                                 .httpStatus(HttpStatus.BAD_REQUEST)
                                                 .message(occasionFilterException.getMessage())
                                                 .localDateTime(LocalDateTime.now())
                                                 .path(request.getDescription(false))
                                                 .build(), HttpStatus.BAD_REQUEST);
    }
}
