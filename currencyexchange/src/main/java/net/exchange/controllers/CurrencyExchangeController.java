package net.exchange.controllers;

import net.exchange.service.CurrencyExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/exchange")
public class CurrencyExchangeController {
    @Autowired
    CurrencyExchangeService currencyExchangeService;

    @RequestMapping(value = "/{currencyCodeFrom}/{currencyCodeTo}/{currencyAmount}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getCurrency(@PathVariable String currencyCodeFrom, @PathVariable String currencyCodeTo, @PathVariable BigDecimal currencyAmount) {
        if (currencyCodeFrom.equals(currencyCodeTo))
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        return new ResponseEntity(currencyExchangeService.exchangeCurrency(currencyCodeFrom, currencyCodeTo, currencyAmount), HttpStatus.ACCEPTED);
    }
}
