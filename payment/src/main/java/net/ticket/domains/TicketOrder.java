package net.ticket.domains;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TicketOrders")
public class TicketOrder {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "OrderId")
    private long orderId;

    @Column(name = "BankAccount", unique = false)
    private long bankAccount;

    @Column(name = "Amount")
    private BigDecimal amount;

    @OneToMany(mappedBy = "ticketOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Customer> customers;

    public Set<Customer> getCustomers() {
        return customers;
    }

    public TicketOrder setCustomers(HashSet<Customer> customers) {
        this.customers = customers;
        return this;
    }

    public long getBankAccount() {
        return bankAccount;
    }

    public TicketOrder setBankAccount(long bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TicketOrder setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }
}
