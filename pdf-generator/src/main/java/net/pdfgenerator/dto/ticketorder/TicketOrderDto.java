package net.pdfgenerator.dto.ticketorder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import net.pdfgenerator.enums.ticket.TicketType;

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
    @NonNull
    private String occasionAddress;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @NonNull
    private LocalDateTime occasionDate;
    @NonNull
    private String occasionName;
    @NonNull
    private long bankAccount;
    @NonNull
    private String currency;
}
