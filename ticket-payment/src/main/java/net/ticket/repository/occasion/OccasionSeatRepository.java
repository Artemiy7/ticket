package net.ticket.repository.occasion;

import net.ticket.constant.OccasionSeatNamedQueriesConstants;
import net.ticket.dto.ticketorder.CustomerTicketDto;
import net.ticket.domain.entity.occasion.OccasionEntity;
import net.ticket.domain.entity.occasion.OccasionSeatEntity;
import net.ticket.ticketexception.occasion.CorruptedOccasionException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Optional;

@Repository
public class OccasionSeatRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<OccasionSeatEntity> findOccasionSeatsByOccasionIdAndSeat(OccasionEntity occasionEntity, CustomerTicketDto customerTicketDto) {
            TypedQuery<OccasionSeatEntity> query = entityManager.createNamedQuery(OccasionSeatNamedQueriesConstants.findOccasionSeatsByOccasionIdAndSeat, OccasionSeatEntity.class);
            query.setParameter("OccasionId", occasionEntity);
            query.setParameter("Seat", customerTicketDto.getSeat());
            if (query.getResultList().size() > 1) {
                throw new CorruptedOccasionException("Two or more same seats");
            }
            try {
                return Optional.ofNullable(query.getResultList().get(0));
            } catch (IndexOutOfBoundsException e) {
                return Optional.empty();
            }
    }

    public void updateOccasionSeatSetIsBooked(OccasionEntity occasionEntity, CustomerTicketDto customerTicketDto) {
        Query query = entityManager.createNamedQuery(OccasionSeatNamedQueriesConstants.updateOccasionSeatSetIsBooked);
        query.setParameter("OccasionId", occasionEntity);
        query.setParameter("Seat", customerTicketDto.getSeat());
        query.executeUpdate();
    }

    public int countNotBookedOccasionSeats(OccasionEntity occasionEntity) {
        Query query = entityManager.createNamedQuery(OccasionSeatNamedQueriesConstants.countNotBookedOccasionSeats);
        query.setParameter("OccasionId", occasionEntity.getOccasionId());
        query.setParameter("IsBooked", false);
        return Integer.parseInt(query.getSingleResult().toString());
    }
}