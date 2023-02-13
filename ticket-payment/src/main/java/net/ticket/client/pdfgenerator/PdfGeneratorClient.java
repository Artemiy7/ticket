package net.ticket.client.pdfgenerator;

import net.ticket.config.web.client.PdfGeneratorClientConfig;
import net.ticket.dto.ticketorder.TicketOrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class PdfGeneratorClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(PdfGeneratorClient.class);
    private final PdfGeneratorClientConfig pdfGeneratorClientConfig;
    private final RestTemplate loadBalancedRestTemplate;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public PdfGeneratorClient(PdfGeneratorClientConfig pdfGeneratorClientConfig,
                              RestTemplate loadBalancedRestTemplate,
                              CircuitBreakerFactory circuitBreakerFactory) {
        this.pdfGeneratorClientConfig = pdfGeneratorClientConfig;
        this.loadBalancedRestTemplate = loadBalancedRestTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public ResponseEntity<byte[]> performRequestToPdfGeneratorService(TicketOrderDto ticketOrderDto) throws HttpServerErrorException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<TicketOrderDto> httpEntity = new HttpEntity<>(ticketOrderDto, headers);
        try {
            ResponseEntity<byte[]> responseEntity = circuitBreakerFactory
                    .create("pdf-generator")
                    .run(() -> loadBalancedRestTemplate.postForEntity(pdfGeneratorClientConfig.getUrn(), httpEntity, byte[].class));
            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                LOGGER.info("Pdf created " + ticketOrderDto.getTicketType().getTicketTypeObject());
                return responseEntity;
            }
        } catch (HttpServerErrorException.InternalServerError e) {
            LOGGER.error("Error on pdf server while printing ticket " + e.getMessage());
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        throw new RuntimeException("Unknown error while printing ticket");
    }
}
