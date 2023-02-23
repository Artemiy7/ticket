package net.ticket.dto.occasion;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import net.ticket.constant.enums.ticket.SeatPlaceType;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OccasionSeatDto {
    @Positive
    private BigDecimal cost;
    @Positive
    private short seat;
    @JsonBackReference
    private OccasionDto occasionDto;
    private boolean isBooked;
    @NonNull
    private SeatPlaceType seatPlaceType;
}
