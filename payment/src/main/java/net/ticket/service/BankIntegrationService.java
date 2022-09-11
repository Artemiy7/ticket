package net.ticket.service;

import net.ticket.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class BankIntegrationService {
    @Value("${bankaccount.uri}")
    String bankAccountUri;
    @Value("${bankpayment.uri}")
    String bankPaymentUri;
    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<String> sendRequestToBankAccount(long bankAccount) {
        String bankUri = String.format(bankAccountUri, bankAccount);
        return restTemplate.getForEntity(bankUri, String.class);
    }

    public ResponseEntity<String> performRequestToBank(OrderDTO orderDTO) {
        return restTemplate.getForEntity(bankUri, String.class);
    }
}
