package net.ticket.controller.ticketorder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.ticket.constant.enums.message.ResponseMessage;
import net.ticket.dto.ticketorder.CustomerTicketDto;
import net.ticket.dto.ticketorder.TicketOrderDto;
import net.ticket.response.error.ErrorResponse;
import net.ticket.service.ticketorder.TicketOrderService;
import net.ticket.ticketexception.occasion.*;
import net.ticket.util.ValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.Optional;

@Api("Create TicketOrder and TicketPdf")
@RestController
@RequestMapping("api/v1/ticket")
public class TicketOrderController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TicketOrderController.class);
    private final TicketOrderService ticketOrderService;
    private final ValidatorUtils validatorUtils;

    public TicketOrderController(TicketOrderService ticketOrderService,
                                 ValidatorUtils validatorUtils) {
        this.ticketOrderService = ticketOrderService;
        this.validatorUtils = validatorUtils;
    }

    @ApiOperation("Create TicketOrder in the DB and return TicketOrderId")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createTicketOrder(@RequestBody TicketOrderDto ticketOrderDto) {
        validatorUtils.validationAfterSerialization(ticketOrderDto);
        ticketOrderDto.getCustomerTicketDto().forEach(validatorUtils::validationAfterSerialization);
        for (CustomerTicketDto customerTicketDto : ticketOrderDto.getCustomerTicketDto()) {
            if (!ticketOrderDto.getTicketType().getSeatPlaceTypes().contains(customerTicketDto.getSeatPlaceType())) {
                LOGGER.error("No such SeatPlaceType " + customerTicketDto.getSeatPlaceType() + " in this TicketType " + customerTicketDto.getTicketOrderDto().getTicketType());
                throw new CorruptedOccasionException("No such SeatPlaceType " + customerTicketDto.getSeatPlaceType() + " in this TicketType " + customerTicketDto.getTicketOrderDto().getTicketType());
            }
        }
        Optional<Long> resultOptional = ticketOrderService.createTicket(ticketOrderDto);

        if (resultOptional.isEmpty()) {
            return new ResponseEntity(ErrorResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(ResponseMessage.OCCASION_NOT_FOUND.getMessage())
                    .localDateTime(LocalDateTime.now())
                    .path("/create")
                    .build(), HttpStatus.BAD_REQUEST);
        }

        long ticketOrderId = resultOptional.get();
        LOGGER.info("TicketOrder created " + ticketOrderId);
        return ResponseEntity.ok().body(ticketOrderId);
    }

    @ApiOperation("Get pdf ticket for every CustomerTicketDto by TicketOrderId")
    @RequestMapping(value = "/PDF/{ticketOrderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> printTicketOrder(@PathVariable long ticketOrderId) throws HttpServerErrorException {
        HttpHeaders headers = new HttpHeaders();
        try {
            ResponseEntity<byte[]> responseEntity = ticketOrderService.generatePdf(ticketOrderId);
            headers.add("Content-Disposition", String.valueOf(responseEntity.getHeaders().get("Content-Disposition")));
            LOGGER.info("Ticket printed");
            return ResponseEntity.ok()
                                 .headers(headers)
                                 .contentType(MediaType.APPLICATION_PDF)
                                 .body(responseEntity.getBody());
        } catch (NullPointerException e) {
            LOGGER.error("TicketOrder data corrupted " + e.getMessage());
            headers.add("Server-Error", "TicketOrder data corrupted");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .headers(headers)
                                 .build();
        } catch (HttpServerErrorException e) {
            LOGGER.error("TicketOrder data corrupted " + "Error while printing ticket");
            headers.add("Server-Error", "Error while printing ticket");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .headers(headers)
                                 .build();
        }
    }
}
