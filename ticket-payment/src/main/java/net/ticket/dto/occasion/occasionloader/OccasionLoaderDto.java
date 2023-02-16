package net.ticket.dto.occasion.occasionloader;

import lombok.*;
import lombok.experimental.SuperBuilder;
import net.ticket.dto.occasion.OccasionDto;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@SuperBuilder
public class OccasionLoaderDto extends OccasionDto {
    private Set<OccasionSeatLoaderDto> occasionSeatLoaderDtoSet;
    private BigDecimal initialPrice;
    private boolean active;
}
