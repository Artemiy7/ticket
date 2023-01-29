package net.status.controller;

import net.status.request.PaymentRequest;
import net.status.util.StatusGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank")
public class StatusCheckController {

    @RequestMapping(value = "/payment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Boolean> postPayment(@RequestBody PaymentRequest paymentRequest) {
        HttpStatus httpStatus = StatusGenerator.generatePaymentStatus();
        if (httpStatus.is2xxSuccessful())
            return ResponseEntity.status(httpStatus).body(true);
        return ResponseEntity.status(httpStatus).body(false);
    }

    @RequestMapping(value = "/account/{bankAccount}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> getAccount(@PathVariable long bankAccount) {
        HttpStatus httpStatus = StatusGenerator.generateAccountStatus();
        if (httpStatus.is2xxSuccessful())
            return ResponseEntity.status(httpStatus).body(true);
        return ResponseEntity.status(httpStatus).body(false);
    }
}
