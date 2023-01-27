package net.ticket.service.occasion.cost.train;

import lombok.Getter;
import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.service.occasion.cost.OccasionCost;
import net.ticket.ticketexception.occasion.CorruptedOccasionSeatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class IntercityTrainCost implements OccasionCost {

    @Getter
    private BigDecimal trainIntercityBusiness;
    @Getter
    private BigDecimal trainIntercityEconomic;
    @Getter
    private BigDecimal dateCoefficient;
    @Getter
    private BigDecimal seatCoefficient;

    @Autowired
    public IntercityTrainCost(@Value("${service.occasion-coefficient.train.intercity.business}") BigDecimal trainIntercityBusiness,
                                   @Value("${service.occasion-coefficient.train.intercity.economic}") BigDecimal trainIntercityEconomic,
                                   @Value("${service.occasion-coefficient.train.intercity.date}") BigDecimal dateCoefficient,
                                   @Value("${service.occasion-coefficient.train.intercity.seat}") BigDecimal seatCoefficient) {
        this.trainIntercityBusiness = trainIntercityBusiness;
        this.trainIntercityEconomic = trainIntercityEconomic;
        this.dateCoefficient = dateCoefficient;
        this.seatCoefficient = seatCoefficient;
    }

    @Override
    public void calculateTicketCost(OccasionSeatDto occasionSeatDto, BigDecimal initialCost) throws CorruptedOccasionSeatException {
        try {
            BigDecimal bigDecimal = BigDecimal.valueOf(occasionSeatDto.getOccasionDto().getNumberOfSeats())
                    .multiply(initialCost)
                    .divide(BigDecimal.valueOf(occasionSeatDto.getOccasionDto().getNotBookedSeats()).divide(seatCoefficient, 2, RoundingMode.HALF_UP), 2, RoundingMode.HALF_UP)
                    .divide(getFieldValueByName(occasionSeatDto.getSeatPlaceType().getSeatPlaceType()), 2, RoundingMode.HALF_UP);
            occasionSeatDto.setCost(bigDecimal);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CorruptedOccasionSeatException(e.getMessage());
        }
    }

    @Override
    public BigDecimal getFieldValueByName(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return (BigDecimal) this.getClass()
                .getDeclaredField(fieldName)
                .get(this);
    }
}
