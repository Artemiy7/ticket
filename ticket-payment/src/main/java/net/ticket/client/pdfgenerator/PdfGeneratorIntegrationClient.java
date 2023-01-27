package net.ticket.client.pdfgenerator;

import net.ticket.dto.ticketorder.TicketOrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class PdfGeneratorIntegrationClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(PdfGeneratorIntegrationClient.class);
    private final String pdfGeneratorGenerateTicketPdfUri;
    private final RestTemplate loadBalancedRestTemplate;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public PdfGeneratorIntegrationClient(@Value("${pdf-generator.generateTicketPdf.url}") String pdfGeneratorGenerateTicketPdfUri,
                                         RestTemplate loadBalancedRestTemplate,
                                         CircuitBreakerFactory circuitBreakerFactory) {
        this.pdfGeneratorGenerateTicketPdfUri = pdfGeneratorGenerateTicketPdfUri;
        this.loadBalancedRestTemplate = loadBalancedRestTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public ResponseEntity<byte[]> performRequestToPdfGeneratorService(TicketOrderDto ticketOrderDto) throws HttpServerErrorException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<TicketOrderDto> httpEntity = new HttpEntity<>(ticketOrderDto, headers);
        try {
            ResponseEntity<byte[]> responseEntity = circuitBreakerFactory
                    .create("pdf-generator")
                    .run(() -> loadBalancedRestTemplate.postForEntity(pdfGeneratorGenerateTicketPdfUri, httpEntity, byte[].class));
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
