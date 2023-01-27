package net.ticket.entity.ticketorder;


import lombok.*;
import net.ticket.enums.ticket.TicketType;


import javax.persistence.*;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "TicketOrder")
public class TicketOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TicketOrderId")
    private long ticketOrderId;

    @Column(name = "BankAccount", unique = false)
    private long bankAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "TicketType")
    private TicketType ticketType;

    @Column(name = "Currency")
    private String currency;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticketOrderEntity", fetch=FetchType.LAZY)
    private Set<CustomerTicketEntity> customersEntitySet;

}
