package net.ticket.transformer.occasion;

import net.ticket.dto.occasion.OccasionDto;
import net.ticket.domain.entity.occasion.OccasionEntity;
import net.ticket.transformer.occasion.occasionseat.OccasionSeatEntityToOccasionSeatDtoTransformer;
import net.ticket.transformer.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OccasionEntityToOccasionDtoTransformer implements Transformer<OccasionEntity, OccasionDto> {
    private final OccasionSeatEntityToOccasionSeatDtoTransformer occasionSeatEntityToOccasionSeatDtoTransformer;

    @Autowired
    public OccasionEntityToOccasionDtoTransformer(OccasionSeatEntityToOccasionSeatDtoTransformer occasionSeatEntityToOccasionSeatDtoTransformer) {
        this.occasionSeatEntityToOccasionSeatDtoTransformer = occasionSeatEntityToOccasionSeatDtoTransformer;
    }

    @Override
    public OccasionDto transform(OccasionEntity occasionEntity) {
        OccasionDto occasionDto = OccasionDto.builder()
                                             .numberOfSeats(occasionEntity.getNumberOfSeats())
                                             .occasionAddress(occasionEntity.getOccasionAddress())
                                             .occasionName(occasionEntity.getOccasionName())
                                             .occasionTime(occasionEntity.getOccasionTime())
                                             .ticketType(occasionEntity.getTicketType())
                                             .build();

        occasionDto.setOccasionSeatDto(occasionEntity.getOccasionSeatEntitySet()
                                                     .stream()
                                                     .map(occasionSeatEntityToOccasionSeatDtoTransformer::transform)
                                                     .collect(Collectors.toSet()));
        occasionDto.getOccasionSeatDto().forEach(occasionSeatDto -> occasionSeatDto.setOccasionDto(occasionDto));
        return occasionDto;
    }

}
