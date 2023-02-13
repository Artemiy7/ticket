package net.ticket.service.occasion.cost.concert;

import net.ticket.config.occasion.cost.concert.ConcertStadionOccasionCostConfig;
import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.service.occasion.cost.OccasionCost;
import net.ticket.ticketexception.occasion.CorruptedOccasionSeatException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ConcertStadionOccasionCost implements OccasionCost {
    private final ConcertStadionOccasionCostConfig concertStadionOccasionCostConfig;

    public ConcertStadionOccasionCost(ConcertStadionOccasionCostConfig concertStadionOccasionCostConfig) {
        this.concertStadionOccasionCostConfig = concertStadionOccasionCostConfig;
    }

    @Override
    public BigDecimal calculateTicketCost(OccasionSeatDto occasionSeatDto, BigDecimal initialCost) throws CorruptedOccasionSeatException {
        try {
            BigDecimal bigDecimal = BigDecimal.valueOf(occasionSeatDto.getOccasionDto().getNumberOfSeats())
                    .multiply(initialCost)
                    .divide(BigDecimal.valueOf(occasionSeatDto.getOccasionDto().getNotBookedSeats()).divide(concertStadionOccasionCostConfig.getSeat(), 2, RoundingMode.HALF_UP), 2, RoundingMode.HALF_UP)
                    .divide(concertStadionOccasionCostConfig.getDate(), 2, RoundingMode.HALF_UP)
                    .divide(concertStadionOccasionCostConfig.getDate(), 2, RoundingMode.HALF_UP)
                    .divide(getSeatPlaceCoefficientBySeatPlaceTypeName(occasionSeatDto.getSeatPlaceType().getSeatPlaceType()), 2, RoundingMode.HALF_UP);
            return bigDecimal;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CorruptedOccasionSeatException(e.getMessage());
        }
    }

    @Override
    public BigDecimal getSeatPlaceCoefficientBySeatPlaceTypeName(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = concertStadionOccasionCostConfig.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (BigDecimal) field.get(concertStadionOccasionCostConfig);
    }
}