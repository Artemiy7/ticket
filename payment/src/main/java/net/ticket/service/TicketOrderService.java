package net.ticket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.ticket.dao.TicketOrderDAO;
import net.ticket.domains.TicketOrder;
import net.ticket.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class TicketOrderService {
    @Value("${defaultcurrency}")
    String defaultCurrency;
    @Autowired
    TicketOrderDAO ticketOrderDAO;
    @Autowired
    CurrencyExchangeIntegrationService currencyExchangeIntegrationService;
    @Autowired
    BankIntegrationService bankIntegrationService;

    @Transactional
    public void saveTicketOrder(OrderDTO ticketOrder) {
        bankIntegrationService.sendRequestToBankAccount(ticketOrder.getBankAccount());
        if (!ticketOrder.getCurrency().equals(defaultCurrency)) {
            ResponseEntity<String> responseEntity =
                    currencyExchangeIntegrationService.sendRequestToCurrencyExchange(ticketOrder.getCurrency(), defaultCurrency, ticketOrder.getAmount());
            BigDecimal exchangedAmount = getAmountFromJsonByDefaultCurrency(responseEntity.getBody());
            ticketOrder.setAmount(exchangedAmount);
        }
        ticketOrderDAO.saveTicketOrder(ticketOrder.toTicketOrder());
    }

    public BigDecimal getAmountFromJsonByDefaultCurrency(String json) {
        JsonNode node;
        ObjectMapper mapper = new ObjectMapper();
        try {
            node = mapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String amount = node.get(defaultCurrency).asText();
        return new BigDecimal(amount);
    }
}