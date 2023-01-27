package net.ticket.repository.ticketorder;

import net.ticket.entity.ticketorder.TicketOrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class TicketOrderRepository {
    @PersistenceContext
    EntityManager entityManager;

    public TicketOrderEntity saveTicketOrder(TicketOrderEntity ticketOrderEntity) {
        entityManager.persist(ticketOrderEntity);
        return ticketOrderEntity;
    }

    public Optional<TicketOrderEntity> findTicketOrder(long id) {
        return Optional.ofNullable(entityManager.find(TicketOrderEntity.class, id));
    }
}
