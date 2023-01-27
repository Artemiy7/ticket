package net.ticket.controller.ticketorder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.ticket.dto.ticketorder.CustomerTicketDto;
import net.ticket.dto.ticketorder.TicketOrderDto;
import net.ticket.service.ticketorder.TicketOrderService;
import net.ticket.ticketexception.occasion.NoSuchOccasionException;
import net.ticket.ticketexception.occasion.NoSuchOccasionSeatException;
import net.ticket.ticketexception.occasion.NoSuchTicketOrderEntityException;
import net.ticket.ticketexception.occasion.OccasionSeatIsBookedException;
import net.ticket.ticketexception.bank.BankServerError;
import net.ticket.ticketexception.bank.NoSuchBankAccount;
import net.ticket.ticketexception.bank.NotEnoughAmountForPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.NoResultException;

@Api("Create TicketOrder")
@RestController
@RequestMapping("/ticket")
public class TicketOrderRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TicketOrderRestController.class);
    private final TicketOrderService ticketOrderService;

    @Autowired
    public TicketOrderRestController(TicketOrderService ticketOrderService) {
        this.ticketOrderService = ticketOrderService;
    }

    @ApiOperation("Create TicketOrder and return TicketOrderId")
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createTicketOrder(@RequestBody TicketOrderDto ticketOrderDto) {
        HttpHeaders headers = new HttpHeaders();
        for (CustomerTicketDto customerTicketDto : ticketOrderDto.getCustomerTicketDto()) {
            if (!ticketOrderDto.getTicketType().getSeatPlaceTypes().contains(customerTicketDto.getSeatPlaceType())) {
                headers.add("Client-Error", "No such SeatPlaceType in this TicketType");
                LOGGER.error("No such SeatPlaceType in this TicketType");
                return ResponseEntity
                        .badRequest()
                        .headers(headers)
                        .build();
            }
        }
        try {
            long ticketOrderId = ticketOrderService.createTicket(ticketOrderDto);
            LOGGER.info("TicketOrder created " + ticketOrderId);
            return ResponseEntity.ok().body(ticketOrderId);
        } catch (NoResultException | NoSuchOccasionException | OccasionSeatIsBookedException | NoSuchBankAccount | NoSuchOccasionSeatException e) {
            e.printStackTrace();
            headers.add("Client-Error", e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .headers(headers)
                    .build();
        } catch (BankServerError e) {
            e.printStackTrace();
            headers.add("Server-Bank", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .build();
        } catch (NotEnoughAmountForPayment e) {
            e.printStackTrace();
            headers.add("Server-Bank-Payment-Error", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.PAYMENT_REQUIRED)
                    .headers(headers)
                    .build();
        }
    }

    @ApiOperation("Get pdf ticket for every CustomerTicketDto")
    @RequestMapping(value = "/PDF/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getTicketOrder(@PathVariable long orderId) throws HttpServerErrorException {
        HttpHeaders headers = new HttpHeaders();
        try {
            ResponseEntity<byte[]> responseEntity = ticketOrderService.generatePdf(orderId);
            headers.add("Content-Disposition", String.valueOf(responseEntity.getHeaders().get("Content-Disposition")));
            LOGGER.info("Ticket printed");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(responseEntity.getBody());
        } catch (NoSuchTicketOrderEntityException e) {
            headers.add("Client-Error", "No such TicketOrder");
            e.printStackTrace();
            return ResponseEntity
                    .notFound()
                    .headers(headers)
                    .build();
        } catch (NullPointerException e) {
            headers.add("Server-Error", "TicketOrder data corrupted");
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .build();
        } catch (HttpServerErrorException e) {
            headers.add("Server-Error", "Error while printing ticket");
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .build();
        }
    }
}
