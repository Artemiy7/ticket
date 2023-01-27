package net.ticket.transformers;

import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.entity.occasion.OccasionSeatEntity;
import net.ticket.enums.ticket.SeatPlaceType;
import org.springframework.stereotype.Component;

@Component
public class OccasionSeatEntityToOccasionSeatDtoTransformer implements Transformer<OccasionSeatEntity, OccasionSeatDto> {
    @Override
    public OccasionSeatDto transform(OccasionSeatEntity entity) {
        return OccasionSeatDto.builder()
                              .isBooked(entity.isBooked())
                              .seat(entity.getSeat())
                              .seatPlaceType(SeatPlaceType.getSeatPlaceTypeByString(entity.getSeatPlaceType().getSeatPlaceType()))
                              .build();
    }
}
