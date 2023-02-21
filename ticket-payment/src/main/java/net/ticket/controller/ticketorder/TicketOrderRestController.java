package net.ticket.controller.ticketorder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.ticket.dto.ticketorder.CustomerTicketDto;
import net.ticket.dto.ticketorder.TicketOrderDto;
import net.ticket.service.ticketorder.TicketOrderService;
import net.ticket.ticketexception.occasion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

@Api("Create TicketOrder")
@RestController
@RequestMapping("/ticket")
public class TicketOrderRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TicketOrderRestController.class);
    private final TicketOrderService ticketOrderService;

    public TicketOrderRestController(TicketOrderService ticketOrderService) {
        this.ticketOrderService = ticketOrderService;
    }

    @ApiOperation("Create TicketOrder and return TicketOrderId")
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createTicketOrder(@RequestBody TicketOrderDto ticketOrderDto) {
        for (CustomerTicketDto customerTicketDto : ticketOrderDto.getCustomerTicketDto()) {
            if (!ticketOrderDto.getTicketType().getSeatPlaceTypes().contains(customerTicketDto.getSeatPlaceType())) {
                LOGGER.error("No such SeatPlaceType " + customerTicketDto.getSeatPlaceType() + " in this TicketType " + customerTicketDto.getTicketOrderDto().getTicketType());
                throw new CorruptedOccasionException("No such SeatPlaceType " + customerTicketDto.getSeatPlaceType() + " in this TicketType " + customerTicketDto.getTicketOrderDto().getTicketType());
            }
        }
        long ticketOrderId = ticketOrderService.createTicket(ticketOrderDto);
        LOGGER.info("TicketOrder created " + ticketOrderId);
        return ResponseEntity.ok().body(ticketOrderId);
    }

    @ApiOperation("Get pdf ticket for every CustomerTicketDto")
    @RequestMapping(value = "/PDF/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> printTicketOrder(@PathVariable long orderId) throws HttpServerErrorException {
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
