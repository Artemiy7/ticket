package net.ticket.entity.ticketorder;


import lombok.*;
import net.ticket.entity.occasion.OccasionSeatEntity;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "CustomerTicket")
public class CustomerTicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerTicketId")
    private long customerTicketId;

    @Column(name = "FirstName", nullable = false)
    private String firstName;

    @Column(name = "LastName", nullable = false)
    private String lastName;

    @Column(name = "Country", nullable = false)
    private String country;

    @JoinColumn(name = "OccasionSeatId", nullable = false)
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private OccasionSeatEntity occasionSeatEntity;

    @Column(name = "Amount", nullable = false)
    private BigDecimal amount;

    @JoinColumn(name = "TicketOrderId", nullable = false)
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private TicketOrderEntity ticketOrderEntity;
}

