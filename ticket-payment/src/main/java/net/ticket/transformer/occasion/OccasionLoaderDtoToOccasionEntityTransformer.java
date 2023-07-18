package net.ticket.transformer.occasion;

import net.ticket.dto.occasion.occasionloader.OccasionLoaderDto;
import net.ticket.domain.entity.occasion.OccasionEntity;
import net.ticket.domain.entity.occasion.OccasionSeatEntity;
import net.ticket.transformer.occasion.occasionseat.OccasionSeatDtoToOccasionSeatEntityTransformer;
import net.ticket.transformer.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OccasionLoaderDtoToOccasionEntityTransformer implements Transformer<OccasionLoaderDto, OccasionEntity> {
    private final OccasionSeatDtoToOccasionSeatEntityTransformer occasionSeatDtoToOccasionSeatEntityTransformer;

    @Autowired
    public OccasionLoaderDtoToOccasionEntityTransformer(OccasionSeatDtoToOccasionSeatEntityTransformer occasionSeatDtoToOccasionSeatEntityTransformer) {
        this.occasionSeatDtoToOccasionSeatEntityTransformer = occasionSeatDtoToOccasionSeatEntityTransformer;
    }

    @Override
    public OccasionEntity transform(OccasionLoaderDto occasionLoaderDto) {
        Set<OccasionSeatEntity> occasionSeatEntitySet = occasionLoaderDto.getOccasionSeatDto().stream()
                .map(occasionSeatDtoToOccasionSeatEntityTransformer::transform).collect(Collectors.toSet());

        return OccasionEntity.builder().occasionName(occasionLoaderDto.getOccasionName())
                .occasionTime(occasionLoaderDto.getOccasionTime())
                .occasionAddress(occasionLoaderDto.getOccasionAddress())
                .isActive(occasionLoaderDto.isActive())
                .numberOfSeats(occasionLoaderDto.getNumberOfSeats())
                .ticketType(occasionLoaderDto.getTicketType())
                .occasionSeatEntitySet(occasionSeatEntitySet)
                .initialCost(occasionLoaderDto.getInitialPrice())
                .build();
    }
}
