package net.ticket.service.occasion.cost.train;

import net.ticket.config.occasion.cost.train.IntercityTrainCostConfig;
import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.service.occasion.cost.OccasionCost;
import net.ticket.ticketexception.occasion.CorruptedOccasionSeatException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class IntercityTrainCost implements OccasionCost {

    private final IntercityTrainCostConfig intercityTrainCostConfig;

    public IntercityTrainCost(IntercityTrainCostConfig intercityTrainCostConfig) {
        this.intercityTrainCostConfig = intercityTrainCostConfig;
    }

    @Override
    public BigDecimal calculateTicketCost(OccasionSeatDto occasionSeatDto, BigDecimal initialCost) throws CorruptedOccasionSeatException {
        try {
            BigDecimal bigDecimal = BigDecimal.valueOf(occasionSeatDto.getOccasionDto().getNumberOfSeats())
                    .multiply(initialCost)
                    .multiply(initialCost)
                    .multiply(initialCost)
                    .multiply(initialCost)
                    .divide(BigDecimal.valueOf(occasionSeatDto.getOccasionDto().getNotBookedSeats()).divide(intercityTrainCostConfig.getSeat(), 2, RoundingMode.HALF_UP), 2, RoundingMode.HALF_UP)
                    .divide(getSeatPlaceCoefficientBySeatPlaceTypeName(occasionSeatDto.getSeatPlaceType().getSeatPlaceType()), 2, RoundingMode.HALF_UP);
            return bigDecimal;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CorruptedOccasionSeatException(e.getMessage());
        }
    }

    @Override
    public BigDecimal getSeatPlaceCoefficientBySeatPlaceTypeName(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = intercityTrainCostConfig.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (BigDecimal) field.get(intercityTrainCostConfig);
    }
}
