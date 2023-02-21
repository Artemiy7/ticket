package net.ticket.dto.ticketorder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.ticket.entity.occasion.OccasionSeatEntity;
import net.ticket.constant.enums.ticket.SeatPlaceType;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerTicketDto {
    private long customerTicketId;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String country;
    @NonNull
    private short seat;
    @NonNull
    private SeatPlaceType seatPlaceType;
    @JsonBackReference
    private TicketOrderDto ticketOrderDto;
    @JsonIgnore
    private OccasionSeatEntity occasionSeat;
}
