package net.ticket.service.occasion.cost;

import net.ticket.ApplicationContextProvider;
import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.ticketexception.occasion.CorruptedOccasionSeatException;

import java.math.BigDecimal;

public interface OccasionCost {
    static OccasionCost getBeanByName(String classType) {
        return (OccasionCost) ApplicationContextProvider.getApplicationContext().getBean(classType);
    }
    BigDecimal calculateTicketCost(OccasionSeatDto occasionSeatDto, BigDecimal initialCost) throws CorruptedOccasionSeatException;

    BigDecimal getSeatPlaceCoefficientBySeatPlaceTypeName(String fieldName) throws NoSuchFieldException, IllegalAccessException;
}
