package net.ticket.dto.occasion.occasionloader;

import lombok.*;
import lombok.experimental.SuperBuilder;
import net.ticket.dto.occasion.OccasionSeatDto;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@SuperBuilder
public class OccasionSeatLoaderDto extends OccasionSeatDto {
    private short numberOfSeatTypes;
    private short from;
    private short to;
}
