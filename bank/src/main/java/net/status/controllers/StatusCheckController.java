package net.status.controllers;

import net.status.dto.PaymentDTO;
import net.status.util.StatusGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank")
public class StatusCheckController {

    @RequestMapping(value = "/payment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<HttpStatus> postPayment(@RequestBody PaymentDTO paymentDTO) {
        HttpStatus httpStatus = StatusGenerator.generatePaymentStatus();
        return new ResponseEntity<>(httpStatus);
    }

    @RequestMapping(value = "/account/{bankAccount}", method = RequestMethod.GET)
    public ResponseEntity<HttpStatus> getAccount(@PathVariable long bankAccount) {
        HttpStatus httpStatus = StatusGenerator.generatePaymentStatus();
        return new ResponseEntity<>(httpStatus);
    }
}
