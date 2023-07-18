package net.ticket.transformer.occasion.occasionseat;

import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.domain.entity.occasion.OccasionSeatEntity;
import net.ticket.constant.enums.ticket.SeatPlaceType;
import net.ticket.transformer.Transformer;
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
