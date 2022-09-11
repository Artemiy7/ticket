package net.exchange.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyExchangeService {

    @Autowired
    CDNIntegrationService cdnIntegrationService;

    public String exchangeCurrency(String currencyCodeFrom, String currencyCodeTo, BigDecimal currencyAmount) {
        ResponseEntity<String> responseEntity = cdnIntegrationService.sendRequestToCDNI(currencyCodeFrom, currencyCodeTo);
        BigDecimal exchangedCurrency = cdnIntegrationService.getExchangedCurrency(responseEntity.getBody(), currencyCodeTo);
        BigDecimal calculatedAmount = calculateCurrencyAmount(exchangedCurrency, currencyAmount);
        return createExchangedJson(currencyCodeTo, calculatedAmount);
    }

    public BigDecimal calculateCurrencyAmount(BigDecimal currencyFrom, BigDecimal currencyTo) {
        return currencyTo.multiply(currencyFrom);
    }

    public String createExchangedJson(String currencyCode, BigDecimal amount) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put(currencyCode, amount);
        return node.toString();
    }
}
