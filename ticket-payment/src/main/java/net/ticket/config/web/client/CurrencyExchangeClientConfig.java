package net.ticket.config.web.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "service.currency-exchange.exchange-currency")
public class CurrencyExchangeClientConfig {
    private String urn;
}
