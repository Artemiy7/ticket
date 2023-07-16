package net.ticket.dto.occasion;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import net.ticket.constant.RegexConstants;
import net.ticket.constant.enums.ticket.TicketType;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OccasionDto {
    @Pattern(regexp = RegexConstants.OCCASION_NAME)
    @NotBlank(message = "occasionName cannot be blank or null")
    private String occasionName;
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime occasionTime;
    private int daysToOccasion;
    @Positive
    private int notBookedSeats;
    @Positive
    private int numberOfSeats;
    @Pattern(regexp = RegexConstants.OCCASION_ADDRESS)
    @NotBlank(message = "occasionAddress cannot be blank or null")
    private String occasionAddress;
    @NonNull
    private TicketType ticketType;
    @JsonManagedReference
    private Set<OccasionSeatDto> occasionSeatDto;

    public OccasionDto setNotBookedSeats(int notBookedSeats) {
        this.notBookedSeats = notBookedSeats;
        return this;
    }

    public OccasionDto setDaysToOccasion(int daysToOccasion) {
        this.daysToOccasion = daysToOccasion;
        return this;
    }
}
