package net.ticket.dto.occasion;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import net.ticket.enums.ticket.SeatPlaceType;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OccasionSeatDto {
    private BigDecimal cost;
    @NonNull
    private short seat;
    @JsonBackReference
    private OccasionDto occasionDto;
    @NonNull
    private boolean isBooked;
    @NonNull
    private SeatPlaceType seatPlaceType;
}
