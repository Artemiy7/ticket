package net.status.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class PaymentRequest {
    long bankAccount;
    BigDecimal amountSum;

    @JsonCreator
    public PaymentRequest(@JsonProperty(value = "bankAccount", required = true) long bankAccount,
                          @JsonProperty(value = "amountSum", required = true) BigDecimal amountSum) {
        this.bankAccount = bankAccount;
        this.amountSum = amountSum;
    }
}
