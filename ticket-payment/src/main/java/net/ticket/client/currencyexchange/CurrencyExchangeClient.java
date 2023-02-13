package net.ticket.client.currencyexchange;

import net.ticket.client.pdfgenerator.PdfGeneratorClient;
import net.ticket.config.web.client.CurrencyExchangeClientConfig;
import net.ticket.response.currencyexchange.CurrencyExchangeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class CurrencyExchangeClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(PdfGeneratorClient.class);
    private final CurrencyExchangeClientConfig currencyExchangeClientConfig;
    private final RestTemplate loadBalancedRestTemplate;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public CurrencyExchangeClient(CurrencyExchangeClientConfig currencyExchangeClientConfig,
                                  RestTemplate loadBalancedRestTemplate,
                                  CircuitBreakerFactory circuitBreakerFactory) {
        this.currencyExchangeClientConfig = currencyExchangeClientConfig;
        this.loadBalancedRestTemplate = loadBalancedRestTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public CurrencyExchangeResponse sendRequestToCurrencyExchange(String currencyCodeFrom, String currencyCodeTo, BigDecimal currencyAmount) throws HttpClientErrorException {
        try {
            ResponseEntity<CurrencyExchangeResponse> responseEntity = circuitBreakerFactory
                    .create("currency-exchange")
                    .run(() -> loadBalancedRestTemplate.getForEntity(String.format(currencyExchangeClientConfig.getUrn(), currencyCodeFrom.toLowerCase(), currencyCodeTo.toLowerCase(), currencyAmount),
                            CurrencyExchangeResponse.class));
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                LOGGER.info("Currency exchanged from " + currencyCodeFrom + " to " + currencyCodeTo);
                return responseEntity.getBody();
            }
        } catch (HttpClientErrorException.BadRequest e) {
            LOGGER.error("Bad request to currency-exchange " + e.getMessage());
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        throw new RuntimeException("Unknown error while performing request to currency-exchange");
    }
}
