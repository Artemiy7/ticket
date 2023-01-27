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

    @Column(name = "FirstName")
    @NonNull
    private String firstName;

    @Column(name = "LastName")
    @NonNull
    private String lastName;

    @Column(name = "Country")
    @NonNull
    private String country;

    @JoinColumn(name = "OccasionSeatId")
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private OccasionSeatEntity occasionSeatEntity;

    @Column(name = "Amount")
    @NonNull
    private BigDecimal amount;

    @JoinColumn(name = "TicketOrderId")
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private TicketOrderEntity ticketOrderEntity;
}

