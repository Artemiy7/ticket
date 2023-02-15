package net.ticket.transformer.occasion;

import net.ticket.dto.occasion.OccasionDto;
import net.ticket.entity.occasion.OccasionEntity;
import net.ticket.transformer.Transformer;
import org.springframework.stereotype.Component;

@Component
public class OccasionEntityToOccasionDtoNoSeatsTransformer implements Transformer<OccasionEntity, OccasionDto> {
    @Override
    public OccasionDto transform(OccasionEntity occasionEntity) {
        return OccasionDto.builder()
                          .numberOfSeats(occasionEntity.getNumberOfSeats())
                          .occasionAddress(occasionEntity.getOccasionAddress())
                          .occasionName(occasionEntity.getOccasionName())
                          .occasionTime(occasionEntity.getOccasionTime())
                          .ticketType(occasionEntity.getTicketType())
                          .build();
    }
}
