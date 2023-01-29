package net.pdfgenerator.dto.ticketorder;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import net.pdfgenerator.enums.ticket.SeatPlaceType;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CustomerTicketDto {
    @NonNull
    private long customerTicketId;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String country;
    private SeatPlaceType seatPlaceType;
    @NonNull
    private short seat;
    @JsonBackReference
    private TicketOrderDto ticketOrderDto;
}
