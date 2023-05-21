package net.ticket.repository.occasion;

import net.ticket.repository.occasion.filter.OccasionFilter;
import net.ticket.dto.ticketorder.TicketOrderDto;
import net.ticket.entity.occasion.OccasionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class OccasionRepository {
    @PersistenceContext
    EntityManager entityManager;
    private final String findOccasionByNameAndDateAndAddressQuery = "SELECT * FROM Occasion os " +
                                                                    "WHERE os.OccasionName=:OccasionName " +
                                                                    "AND os.OccasionTime=:OccasionTime " +
                                                                    "AND os.TicketType=:TicketType " +
                                                                    "AND os.OccasionAddress=:OccasionAddress";

    public Optional<OccasionEntity> getOccasionById(long id) {
        return Optional.ofNullable(entityManager.find(OccasionEntity.class, id));
    }

    public Optional<List<OccasionEntity>> findOccasionsByFilter(Map<OccasionFilter, List<String>> occasionFilterMap, short size, short resultOrder) throws IllegalArgumentException {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OccasionEntity> criteria = builder.createQuery(OccasionEntity.class);
        Root<OccasionEntity> root = criteria.from(OccasionEntity.class);
        List<Predicate> filterRestrictions = new ArrayList<>();

        for (Map.Entry<OccasionFilter, List<String>> entry : occasionFilterMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                List<Predicate> sameTypePredicatesList = new ArrayList<>();
                entry.getValue().forEach(value -> {
                    sameTypePredicatesList.add(entry.getKey().filterOccasion(root, criteria, builder, value));
                });
                filterRestrictions.add(builder.or(sameTypePredicatesList.toArray(new Predicate[sameTypePredicatesList.size()])));
            } else {
                filterRestrictions.add(builder.and(entry.getKey().filterOccasion(root, criteria, builder, entry.getValue().get(0))));
            }
        }
        criteria.where(builder.and(filterRestrictions.toArray(new Predicate[filterRestrictions.size()])));
        TypedQuery<OccasionEntity> query = entityManager.createQuery(criteria);
        query.setFirstResult(resultOrder);
        query.setMaxResults(size);

        List<OccasionEntity> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(resultList);
    }

    public Optional<OccasionEntity> findOccasionByNameAndDateAndAddress(TicketOrderDto ticketOrderDto) {
        Query query = entityManager.createNativeQuery(findOccasionByNameAndDateAndAddressQuery, OccasionEntity.class);
        query.setParameter("OccasionName", ticketOrderDto.getOccasionName());
        query.setParameter("OccasionTime", ticketOrderDto.getOccasionDate().toString());
        query.setParameter("TicketType", ticketOrderDto.getTicketType().name());
        query.setParameter("OccasionAddress", ticketOrderDto.getOccasionAddress());
        try {
            return Optional.ofNullable((OccasionEntity) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<OccasionEntity> findOccasionByNameAndDateAndAddress(OccasionEntity occasionEntity) {
        Query query = entityManager.createNativeQuery(findOccasionByNameAndDateAndAddressQuery, OccasionEntity.class);
        query.setParameter("OccasionName", occasionEntity.getOccasionName());
        query.setParameter("OccasionTime", occasionEntity.getOccasionTime().toString());
        query.setParameter("TicketType", occasionEntity.getTicketType().name());
        query.setParameter("OccasionAddress", occasionEntity.getOccasionAddress());
        try {
            return Optional.ofNullable((OccasionEntity) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public short findNotBookedSeatForOccasion(OccasionEntity occasionEntity) {
        Query query = entityManager.createNativeQuery("SELECT COUNT(os.OccasionId) FROM OccasionSeat os WHERE os.OccasionId=:OccasionId AND os.IsBooked=:IsBooked");
        query.setParameter("OccasionId", occasionEntity.getOccasionId());
        query.setParameter("IsBooked", false);
        return Short.parseShort(query.getSingleResult().toString( ));
    }

    public void persistOccasion(OccasionEntity occasionEntity) {
        entityManager.persist(occasionEntity);
    }

    public void setOutdatedOccasionNotActive(LocalDateTime localDateTime) {
        Query query = entityManager.createNativeQuery("UPDATE Occasion os SET IsActive = false WHERE os.OccasionTime>=:OccasionTime", OccasionEntity.class);
        query.setParameter("OccasionTime", localDateTime);
        query.executeUpdate();
    }
}
