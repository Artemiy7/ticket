package net.ticket.entity.occasion;

import lombok.*;
import net.ticket.entity.ticketorder.CustomerTicketEntity;
import net.ticket.enums.ticket.SeatPlaceType;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "OccasionSeat")
public class OccasionSeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OccasionSeatId")
    private long occasionSeatId;

    @Column(name = "Seat")
    private short seat;

    @Enumerated(EnumType.STRING)
    @Column(name = "SeatPlaceType")
    private SeatPlaceType seatPlaceType;

    @OneToOne(mappedBy = "occasionSeatEntity")
    private CustomerTicketEntity customerTicketEntity;

    @JoinColumn(name = "OccasionId", referencedColumnName = "OccasionId")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OccasionEntity occasionEntity;

    @Column(name = "IsBooked")
    private boolean isBooked;
}
