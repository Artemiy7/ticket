package net.exchange.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CurrencyExchangeResponse {
    private String currency;
    private BigDecimal amount;
}
