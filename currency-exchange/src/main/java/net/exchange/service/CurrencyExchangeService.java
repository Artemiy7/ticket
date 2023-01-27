package net.exchange.service;

import net.exchange.client.CDNClient;
import net.exchange.response.CurrencyExchangeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyExchangeService {

    private final CDNClient cdnClient;

    @Autowired
    public CurrencyExchangeService(CDNClient cdnClient) {
        this.cdnClient = cdnClient;
    }

    public CurrencyExchangeResponse exchangeCurrency(String currencyCodeFrom, String currencyCodeTo, BigDecimal currencyAmount) {
        ResponseEntity<String> responseEntity = cdnClient.sendRequestToCDNI(currencyCodeFrom, currencyCodeTo);
        BigDecimal exchangedCurrency = cdnClient.getExchangedCurrency(responseEntity.getBody(), currencyCodeTo);
        BigDecimal calculatedAmount = calculateCurrencyAmount(exchangedCurrency, currencyAmount);
        return buildResponse(currencyCodeTo, calculatedAmount);
    }

    public BigDecimal calculateCurrencyAmount(BigDecimal currencyFrom, BigDecimal currencyTo) {
        return currencyTo.multiply(currencyFrom);
    }

    public CurrencyExchangeResponse buildResponse(String currencyCode, BigDecimal amount) {
        return CurrencyExchangeResponse.builder()
                                       .currency(currencyCode)
                                       .amount(amount)
                                       .build();
    }
}
