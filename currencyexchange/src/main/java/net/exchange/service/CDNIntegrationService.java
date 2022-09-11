package net.exchange.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class CDNIntegrationService {
    @Value("${exchange.uri}")
    String serverUrl;
    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<String> sendRequestToCDNI(String currencyCodeFrom, String currencyCodeTo) {
        String echangeServerUrl = String.format(serverUrl, currencyCodeFrom.toLowerCase(), currencyCodeTo.toLowerCase());
        return restTemplate.getForEntity(echangeServerUrl, String.class);
    }

    public BigDecimal getExchangedCurrency(String responce, String currencyCodeTo) {
        JsonNode node;
        ObjectMapper mapper = new ObjectMapper();
        try {
            node = mapper.readTree(responce);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String amount = node.get(currencyCodeTo).asText();
        return new BigDecimal(amount);
    }

}
