package net.ticket.controller.ticketorder.util;

import net.ticket.controller.ticketorder.TicketOrderRestController;
import net.ticket.response.error.ErrorResponse;
import net.ticket.ticketexception.bank.BankServerError;
import net.ticket.ticketexception.bank.InvalidBankAccount;
import net.ticket.ticketexception.bank.NoSuchBankAccount;
import net.ticket.ticketexception.bank.NotEnoughAmountForPayment;
import net.ticket.ticketexception.occasion.*;
import net.ticket.ticketexception.ticketorder.NoSuchTicketOrderEntityException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice(basePackageClasses = TicketOrderRestController.class)
public class TicketOrderExceptionHandler {

    @ExceptionHandler({NoSuchOccasionException.class, NoSuchOccasionSeatException.class, OccasionSeatIsBookedException.class, CorruptedOccasionException.class})
    public ResponseEntity<ErrorResponse> handleOccasionException(OccasionException occasionException, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Occasion-Error-Message", occasionException.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                                                 .httpStatus(HttpStatus.BAD_REQUEST)
                                                 .message(occasionException.getMessage())
                                                 .localDateTime(LocalDateTime.now())
                                                 .path(request.getDescription(false))
                                                 .build(), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoSuchBankAccount.class, InvalidBankAccount.class})
    public ResponseEntity<ErrorResponse> handleBankException(RuntimeException runtimeException, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Bank-Error", runtimeException.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                                                 .httpStatus(HttpStatus.BAD_REQUEST)
                                                 .message(runtimeException.getMessage())
                                                 .localDateTime(LocalDateTime.now())
                                                 .path(request.getDescription(false))
                                                 .build(), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotEnoughAmountForPayment.class})
    public ResponseEntity<ErrorResponse> handleNotEnoughAmountForPayment(NotEnoughAmountForPayment notEnoughAmountForPayment, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Bank-Payment-Error", notEnoughAmountForPayment.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                                                 .httpStatus(HttpStatus.BAD_REQUEST)
                                                 .message(notEnoughAmountForPayment.getMessage())
                                                 .localDateTime(LocalDateTime.now())
                                                 .path(request.getDescription(false))
                                                 .build(), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BankServerError.class})
    public ResponseEntity<ErrorResponse> handleBankServerError(BankServerError bankServerError, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server-Bank-Payment-Error", bankServerError.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                                                 .httpStatus(HttpStatus.BAD_REQUEST)
                                                 .message(bankServerError.getMessage())
                                                 .localDateTime(LocalDateTime.now())
                                                 .path(request.getDescription(false))
                                                 .build(), headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoSuchTicketOrderEntityException.class})
    public ResponseEntity<ErrorResponse> handleNoSuchTicketOrderEntityException(NoSuchTicketOrderEntityException noSuchTicketOrderEntityException, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Ticket-Order-Error", noSuchTicketOrderEntityException.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                                                 .httpStatus(HttpStatus.NOT_FOUND)
                                                 .message(noSuchTicketOrderEntityException.getMessage())
                                                 .localDateTime(LocalDateTime.now())
                                                 .path(request.getDescription(false))
                                                 .build(), headers, HttpStatus.NOT_FOUND);
    }
}
