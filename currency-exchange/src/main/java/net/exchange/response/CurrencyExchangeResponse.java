package net.exchange.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class CurrencyExchangeResponse {
    @NonNull
    private String currency;
    @NonNull
    private BigDecimal amount;
}
