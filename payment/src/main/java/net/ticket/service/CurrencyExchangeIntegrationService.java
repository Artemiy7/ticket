package net.ticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class CurrencyExchangeIntegrationService {
    @Value("${exchange.uri}")
    String serverUrl;
    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<String> sendRequestToCurrencyExchange(String currencyCodeFrom, String currencyCodeTo, BigDecimal currencyAmount) {
        String echangeServerUrl = serverUrl;
        echangeServerUrl = String.format(echangeServerUrl, currencyCodeFrom.toLowerCase(), currencyCodeTo.toLowerCase(), currencyAmount);
        return restTemplate.getForEntity(echangeServerUrl, String.class);
    }
}
