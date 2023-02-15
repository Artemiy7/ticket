package net.ticket.transformer.occasion.occasionseat;

import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.entity.occasion.OccasionSeatEntity;

import net.ticket.transformer.Transformer;
import org.springframework.stereotype.Component;

@Component
public class OccasionSeatDtoToOccasionSeatEntityTransformer implements Transformer<OccasionSeatDto, OccasionSeatEntity> {
    @Override
    public OccasionSeatEntity transform(OccasionSeatDto occasionSeatDto) {
        return OccasionSeatEntity.builder()
                                 .seat(occasionSeatDto.getSeat())
                                 .seatPlaceType(occasionSeatDto.getSeatPlaceType())
                                 .isBooked(occasionSeatDto.isBooked())
                                 .build();
    }
}
