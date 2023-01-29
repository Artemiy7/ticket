package net.ticket.request.payment;
import lombok.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PaymentRequest {
    @NonNull
    private long bankAccount;
    @NonNull
    private long ticketOrderId;
    @NonNull
    private BigDecimal amountSum;
}