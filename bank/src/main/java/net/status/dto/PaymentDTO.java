package net.status.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class PaymentDTO {
    long bankAccount;
    BigDecimal amountSum;

    @JsonCreator
    public PaymentDTO(@JsonProperty(value = "bankAccount", required = true) long bankAccount,
                      @JsonProperty(value = "amountSum", required = true) BigDecimal amountSum) {
        this.bankAccount = bankAccount;
        this.amountSum = amountSum;
    }
}
