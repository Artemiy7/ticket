package net.ticket.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.ticket.domains.Customer;
import net.ticket.domains.TicketOrder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class OrderDTO {
    Set<CustomerDTO> customersDTO;
    BigDecimal amount;
    long bankAccount;
    String currency;
    @JsonIgnore
    TicketOrder ticketOrder;

    public TicketOrder toTicketOrder(){
        return ticketOrder.setAmount(amount)
                          .setBankAccount(bankAccount)
                          .setCustomers(toCustomerSet());
    }

    public HashSet<Customer> toCustomerSet(){
        HashSet<Customer> customers = new HashSet<>();
        for (CustomerDTO customerDTO : customersDTO) {
            customers.add(customerDTO.toCustomer(ticketOrder));
        }
        return customers;
    }

    @JsonCreator
    public OrderDTO(@JsonProperty(value= "customers", required = true) Set<CustomerDTO> customersDTO,
                    @JsonProperty(value= "amount", required = true) BigDecimal amount,
                    @JsonProperty(value= "bankAccount", required = true) long bankAccount,
                    @JsonProperty(value= "currency", required = true) String currency) {
        this.customersDTO = customersDTO;
        this.amount = amount;
        this.bankAccount = bankAccount;
        this.currency = currency;
        this.ticketOrder = new TicketOrder();
    }

    public long getBankAccount() {
        return bankAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }
}
