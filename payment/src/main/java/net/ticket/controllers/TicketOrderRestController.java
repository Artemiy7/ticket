package net.ticket.controllers;

import net.ticket.dao.TicketOrderDAO;
import net.ticket.dto.OrderDTO;
import net.ticket.service.TicketOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/ticket")
public class TicketOrderRestController {
    @Autowired
    TicketOrderService ticketOrderService;

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createTicketOrder(@RequestBody OrderDTO orderDTO) {
        ticketOrderService.saveTicketOrder(orderDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
