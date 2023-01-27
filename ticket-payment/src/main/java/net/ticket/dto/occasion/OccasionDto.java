package net.ticket.dto.occasion;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import net.ticket.enums.ticket.TicketType;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OccasionDto {
    @NonNull
    private String occasionName;
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime occasionTime;
    @NonNull
    private short daysToOccasion;
    @NonNull
    private short notBookedSeats;
    @NonNull
    private short numberOfSeats;
    @NonNull
    private String occasionAddress;
    @NonNull
    private TicketType ticketType;
    @JsonManagedReference
    private Set<OccasionSeatDto> occasionSeatDto;

    public OccasionDto setNotBookedSeats(short notBookedSeats) {
        this.notBookedSeats = notBookedSeats;
        return this;
    }

    public OccasionDto setDaysToOccasion(short daysToOccasion) {
        this.daysToOccasion = daysToOccasion;
        return this;
    }
}
