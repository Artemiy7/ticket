package net.ticket.dto.ticketorder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.ticket.domain.entity.occasion.OccasionSeatEntity;
import net.ticket.constant.enums.ticket.SeatPlaceType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerTicketDto {
    private long customerTicketId;
    @Positive
    @NotNull
    private BigDecimal amount;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String country;
    @Positive
    private int seat;
    @NotNull
    private SeatPlaceType seatPlaceType;
    @JsonBackReference
    private TicketOrderDto ticketOrderDto;
    @JsonIgnore
    private OccasionSeatEntity occasionSeat;
}
