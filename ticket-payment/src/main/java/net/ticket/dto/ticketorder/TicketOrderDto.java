package net.ticket.dto.ticketorder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import net.ticket.constant.RegexConstants;
import net.ticket.constant.enums.ticket.TicketType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TicketOrderDto {
    @NonNull
    private TicketType ticketType;
    @NonNull
    @JsonManagedReference
    private Set<CustomerTicketDto> customerTicketDto;
    @Pattern(regexp = RegexConstants.OCCASION_ADDRESS)
    @NotBlank
    private String occasionAddress;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @NonNull
    private LocalDateTime occasionDate;
    @Pattern(regexp = RegexConstants.OCCASION_NAME)
    @NotBlank
    private String occasionName;
    @Positive
    private long bankAccount;
    @Pattern(regexp = RegexConstants.CURRENCY)
    @NotBlank
    private String currency;
}

