package net.exchange.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class CDNClient {
    private final String serverUrl;
    private final RestTemplate restTemplate;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public CDNClient(@Value("${exchange.uri}") String serverUrl,
                     RestTemplate restTemplate,
                     CircuitBreakerFactory circuitBreakerFactory) {
        this.serverUrl = serverUrl;
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public ResponseEntity<String> sendRequestToCDNI(String currencyCodeFrom, String currencyCodeTo) {
        String exchangeServerUrl = String.format(serverUrl, currencyCodeFrom.toLowerCase(), currencyCodeTo.toLowerCase());
        return circuitBreakerFactory.create("currency-exchange").run(() -> restTemplate.getForEntity(exchangeServerUrl, String.class));
    }

    public BigDecimal getExchangedCurrency(String response, String currencyCodeTo) {
        JsonNode node;
        ObjectMapper mapper = new ObjectMapper();
        try {
            node = mapper.readTree(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String amount = node.get(currencyCodeTo).asText();
        return new BigDecimal(amount);
    }

}
