package net.exchange.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.exchange.response.CurrencyExchangeResponse;
import net.exchange.service.CurrencyExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Api
@RestController
@RequestMapping("/exchange")
public class CurrencyExchangeController {
    private final CurrencyExchangeService currencyExchangeService;

    @Autowired
    public CurrencyExchangeController(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    @ApiOperation("Perform request to third party converter and return calculated amount")
    @RequestMapping(value = "/{currencyCodeFrom}/{currencyCodeTo}/{currencyAmount}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CurrencyExchangeResponse> getCurrency(@PathVariable String currencyCodeFrom,
                                                                @PathVariable String currencyCodeTo,
                                                                @PathVariable BigDecimal currencyAmount) {
        if (currencyCodeFrom.equals(currencyCodeTo))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok()
                             .body(currencyExchangeService.exchangeCurrency(currencyCodeFrom, currencyCodeTo, currencyAmount));
    }
}
