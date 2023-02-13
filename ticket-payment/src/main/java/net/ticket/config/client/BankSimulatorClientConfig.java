package net.ticket.config.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "service.bank.url")
public class BankSimulatorClientConfig {
    String accountUrl;
    String paymentUrl;
}
