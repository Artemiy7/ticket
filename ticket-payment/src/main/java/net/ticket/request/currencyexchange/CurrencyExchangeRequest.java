package net.ticket.request.currencyexchange;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CurrencyExchangeRequest {
    @NonNull
    BigDecimal calculatedAmount;
}
