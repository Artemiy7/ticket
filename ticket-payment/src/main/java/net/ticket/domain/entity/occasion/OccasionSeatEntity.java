package net.ticket.domain.entity.occasion;

import lombok.*;
import net.ticket.constant.OccasionSeatNamedQueriesConstants;
import net.ticket.domain.entity.ticketorder.CustomerTicketEntity;
import net.ticket.constant.enums.ticket.SeatPlaceType;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@NamedQueries({
        @NamedQuery(name = OccasionSeatNamedQueriesConstants.findOccasionSeatsByOccasionIdAndSeat,
        query = "SELECT os FROM OccasionSeatEntity os WHERE os.occasionEntity=:OccasionId AND os.seat=:Seat"),
        @NamedQuery(name = OccasionSeatNamedQueriesConstants.countNotBookedOccasionSeats,
        query = "SELECT COUNT(os.occasionEntity) FROM OccasionSeatEntity os WHERE os.occasionEntity=:OccasionId AND os.isBooked=:IsBooked"),
        @NamedQuery(name = OccasionSeatNamedQueriesConstants.updateOccasionSeatSetIsBooked,
        query = "UPDATE OccasionSeatEntity os SET os.isBooked = true WHERE os.occasionEntity=:OccasionId AND os.seat=:Seat")
})
@Entity
@Table(name = "OccasionSeat")
public class OccasionSeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OccasionSeatId")
    private long occasionSeatId;

    @Column(name = "Seat")
    private int seat;

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
