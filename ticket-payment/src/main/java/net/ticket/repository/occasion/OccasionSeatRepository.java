package net.ticket.repository.occasion;

import net.ticket.dto.ticketorder.CustomerTicketDto;
import net.ticket.entity.occasion.OccasionEntity;
import net.ticket.entity.occasion.OccasionSeatEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Optional;

@Repository
public class OccasionSeatRepository {
    @PersistenceContext
    EntityManager entityManager;

    public Optional<OccasionSeatEntity> findOccasionSeats(OccasionEntity occasionEntity, CustomerTicketDto customerTicketDto) {
            Query query = entityManager.createNativeQuery("SELECT * FROM OccasionSeat os WHERE" +
                    " os.OccasionId=:OccasionId" +
                    " AND os.Seat=:Seat", OccasionSeatEntity.class);
            query.setParameter("OccasionId", occasionEntity.getOccasionId());
            query.setParameter("Seat", customerTicketDto.getSeat());
            if (query.getResultList().size() > 1) {
                throw new RuntimeException("OccasionSeat is corrupted");
            }
            try {
                return Optional.ofNullable((OccasionSeatEntity) query.getResultList().get(0));
            } catch (IndexOutOfBoundsException e) {
                return Optional.empty();
            }
    }

    public void updateOccasionSeatIsBooked(OccasionEntity occasionEntity, CustomerTicketDto customerTicketDto) {
        Query query = entityManager.createNativeQuery("UPDATE OccasionSeat os SET os.IsBooked = true" +
                " WHERE os.OccasionId=:OccasionId" +
                " AND os.Seat=:Seat");
        query.setParameter("OccasionId", occasionEntity.getOccasionId());
        query.setParameter("Seat", customerTicketDto.getSeat());
        query.executeUpdate();
    }
}
