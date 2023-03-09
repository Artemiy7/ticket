package net.ticket.service.occasion.cost.concert;

import net.ticket.config.occasion.cost.concert.ConcertClubOccasionCostConfig;
import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.service.occasion.cost.OccasionCost;
import net.ticket.ticketexception.occasion.CorruptedOccasionSeatException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
class ConcertClubOccasionCost implements OccasionCost {

    private final ConcertClubOccasionCostConfig concertClubOccasionCostConfig;

    public ConcertClubOccasionCost(ConcertClubOccasionCostConfig concertClubOccasionCostConfig) {
        this.concertClubOccasionCostConfig = concertClubOccasionCostConfig;
    }

    @Override
    public BigDecimal calculateTicketCost(OccasionSeatDto occasionSeatDto, BigDecimal initialCost) throws CorruptedOccasionSeatException {
        try {
            BigDecimal bigDecimal = BigDecimal.valueOf(occasionSeatDto.getOccasionDto().getNumberOfSeats())
                    .divide(BigDecimal.valueOf(occasionSeatDto.getOccasionDto().getNotBookedSeats())
                            .multiply(concertClubOccasionCostConfig.getSeat())
                            .multiply(concertClubOccasionCostConfig.getSeat())
                            .multiply(concertClubOccasionCostConfig.getSeat()), 2, RoundingMode.HALF_UP)
                    .divide(concertClubOccasionCostConfig.getDate(), 2, RoundingMode.HALF_UP)
                    .divide(getSeatPlaceCoefficientBySeatPlaceTypeName(occasionSeatDto.getSeatPlaceType().getSeatPlaceType()), 2, RoundingMode.HALF_UP);
            return bigDecimal;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CorruptedOccasionSeatException(e.getMessage());
        }
    }

    @Override
    public BigDecimal getSeatPlaceCoefficientBySeatPlaceTypeName(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = concertClubOccasionCostConfig.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (BigDecimal) field.get(concertClubOccasionCostConfig);
    }
}
