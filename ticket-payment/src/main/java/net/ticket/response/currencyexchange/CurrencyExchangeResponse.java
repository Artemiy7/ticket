package net.ticket.response.currencyexchange;

import lombok.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@NoArgsConstructor
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
