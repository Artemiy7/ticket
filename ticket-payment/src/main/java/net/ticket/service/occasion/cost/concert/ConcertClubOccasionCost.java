package net.ticket.service.occasion.cost.concert;

import lombok.Getter;
import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.service.occasion.cost.OccasionCost;
import net.ticket.ticketexception.occasion.CorruptedOccasionSeatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
class ConcertClubOccasionCost implements OccasionCost {

    @Getter
    private BigDecimal concertClubVip;
    @Getter
    private BigDecimal concertClubFan;
    @Getter
    private BigDecimal concertClubBalcony;
    @Getter
    private BigDecimal dateCoefficient;
    @Getter
    private BigDecimal seatCoefficient;

    @Autowired
    public ConcertClubOccasionCost(@Value("${service.occasion-coefficient.concert.club.balcony}") BigDecimal concertClubVip,
                                   @Value("${service.occasion-coefficient.concert.club.vip}") BigDecimal concertClubFan,
                                   @Value("${service.occasion-coefficient.concert.club.fan}") BigDecimal concertClubBalcony,
                                   @Value("${service.occasion-coefficient.concert.club.date}") BigDecimal dateCoefficient,
                                   @Value("${service.occasion-coefficient.concert.club.seat}") BigDecimal seatCoefficient) {
        this.concertClubVip = concertClubVip;
        this.concertClubFan = concertClubFan;
        this.concertClubBalcony = concertClubBalcony;
        this.dateCoefficient = dateCoefficient;
        this.seatCoefficient = seatCoefficient;
    }

    @Override
    public BigDecimal calculateTicketCost(OccasionSeatDto occasionSeatDto, BigDecimal initialCost) throws CorruptedOccasionSeatException {
        try {
            BigDecimal bigDecimal = BigDecimal.valueOf(occasionSeatDto.getOccasionDto().getNumberOfSeats())
                    .divide(initialCost)
                    .divide(BigDecimal.valueOf(occasionSeatDto.getOccasionDto().getNotBookedSeats()).divide(seatCoefficient, 2, RoundingMode.HALF_UP), 2, RoundingMode.HALF_UP)
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
