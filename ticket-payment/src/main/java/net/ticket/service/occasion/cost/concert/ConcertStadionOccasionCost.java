package net.ticket.service.occasion.cost.concert;

import lombok.Getter;
import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.service.occasion.cost.OccasionCost;
import net.ticket.ticketexception.occasion.CorruptedOccasionSeatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConcertStadionOccasionCost implements OccasionCost {

    @Getter
    private BigDecimal concertStadionVip;
    @Getter
    private BigDecimal concertStadionFan;
    @Getter
    private BigDecimal concertStadionSeating;
    @Getter
    private BigDecimal dateCoefficient;
    @Getter
    private BigDecimal seatCoefficient;

    @Autowired
    public ConcertStadionOccasionCost(@Value("${service.occasion-coefficient.concert.stadion.vip}") BigDecimal concertStadionVip,
                                      @Value("${service.occasion-coefficient.concert.stadion.fan}") BigDecimal concertStadionFan,
                                      @Value("${service.occasion-coefficient.concert.stadion.seating}") BigDecimal concertStadionSeating,
                                      @Value("${service.occasion-coefficient.concert.stadion.date}") BigDecimal dateCoefficient,
                                      @Value("${service.occasion-coefficient.concert.stadion.seat}") BigDecimal seatCoefficient) {
        this.concertStadionVip = concertStadionVip;
        this.concertStadionFan = concertStadionFan;
        this.concertStadionSeating = concertStadionSeating;
        this.dateCoefficient = dateCoefficient;
        this.seatCoefficient = seatCoefficient;
    }

    @Override
    public BigDecimal calculateTicketCost(OccasionSeatDto occasionSeatDto, BigDecimal initialCost) throws CorruptedOccasionSeatException {
        try {
            BigDecimal bigDecimal = BigDecimal.valueOf(occasionSeatDto.getOccasionDto().getNumberOfSeats())
                    .multiply(initialCost)
                    .divide(BigDecimal.valueOf(occasionSeatDto.getOccasionDto().getNotBookedSeats()).divide(seatCoefficient, 2, RoundingMode.HALF_UP), 2, RoundingMode.HALF_UP)
                    .divide(dateCoefficient, 2, RoundingMode.HALF_UP)
                    .divide(dateCoefficient, 2, RoundingMode.HALF_UP)
                    .divide(getFieldValueByName(occasionSeatDto.getSeatPlaceType().getSeatPlaceType()), 2, RoundingMode.HALF_UP);
            return bigDecimal;
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