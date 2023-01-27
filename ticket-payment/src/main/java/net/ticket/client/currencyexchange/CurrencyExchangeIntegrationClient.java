package net.ticket.client.currencyexchange;

import net.ticket.client.pdfgenerator.PdfGeneratorIntegrationClient;
import net.ticket.response.currencyexchange.CurrencyExchangeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class CurrencyExchangeIntegrationClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(PdfGeneratorIntegrationClient.class);
    private final String serverUrl;
    private final RestTemplate loadBalancedRestTemplate;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public CurrencyExchangeIntegrationClient(@Value("${exchange.uri}") String serverUrl,
                                         RestTemplate loadBalancedRestTemplate,
                                         CircuitBreakerFactory circuitBreakerFactory) {
        this.serverUrl = serverUrl;
        this.loadBalancedRestTemplate = loadBalancedRestTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public CurrencyExchangeResponse sendRequestToCurrencyExchange(String currencyCodeFrom, String currencyCodeTo, BigDecimal currencyAmount) throws HttpClientErrorException {
        try {
            ResponseEntity<CurrencyExchangeResponse> responseEntity = circuitBreakerFactory
                    .create("currency-exchange")
                    .run(() -> loadBalancedRestTemplate.getForEntity(String.format(serverUrl, currencyCodeFrom.toLowerCase(), currencyCodeTo.toLowerCase(), currencyAmount),
                            CurrencyExchangeResponse.class));
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                LOGGER.info("Currency exchanged ");
                return responseEntity.getBody();
            }
        } catch (HttpClientErrorException.BadRequest e) {
            LOGGER.error("Bad request to currency-exchange " + e.getMessage());
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        throw new RuntimeException("Unknown error while performing request to currency-exchange");
    }
}
