package net.ticket.repository.occasion;

import net.ticket.constant.enums.search.occasion.OccasionQueryParameterOperation;
import net.ticket.domain.entity.occasion.OccasionSeatEntity;
import net.ticket.domain.pagination.PageAndSortingObject;
import net.ticket.dto.ticketorder.TicketOrderDto;
import net.ticket.domain.entity.occasion.OccasionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class OccasionRepository {
    @PersistenceContext
    EntityManager entityManager;
    private final String findOccasionByNameAndDateAndAddressQuery = "SELECT os FROM Occasion os WHERE os.OccasionName=:OccasionName AND os.OccasionTime=:OccasionTime " +
                                                                    "AND os.TicketType=:TicketType AND os.OccasionAddress=:OccasionAddress";

    public Optional<OccasionEntity> getOccasionById(long id) {
        return Optional.ofNullable(entityManager.find(OccasionEntity.class, id));
    }

    public Optional<List<OccasionEntity>> findOccasionsByParametersMap(Map<OccasionQueryParameterOperation, List<String>> occasionSearchMap, PageAndSortingObject pageAndSortingObject) throws IllegalArgumentException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OccasionEntity> criteriaQuery = criteriaBuilder.createQuery(OccasionEntity.class);
        Root<OccasionEntity> root = criteriaQuery.from(OccasionEntity.class);
        List<Predicate> searchRestrictions = new ArrayList<>();
        Join<OccasionEntity, OccasionSeatEntity> join = null;

        for (Map.Entry<OccasionQueryParameterOperation, List<String>> entry : occasionSearchMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                List<Predicate> sameTypePredicatesList = new ArrayList<>();
                entry.getValue().forEach(value -> {
                    sameTypePredicatesList.add(entry.getKey().filterOccasion(root, criteriaQuery, criteriaBuilder, join, value));
                });
                searchRestrictions.add(criteriaBuilder.or(sameTypePredicatesList.toArray(new Predicate[sameTypePredicatesList.size()])));
            } else {
                searchRestrictions.add(criteriaBuilder.and(entry.getKey().filterOccasion(root, criteriaQuery, criteriaBuilder, join, entry.getValue().get(0))));
            }
        }
        criteriaQuery.where(criteriaBuilder.and(searchRestrictions.toArray(new Predicate[searchRestrictions.size()])));

        if (pageAndSortingObject.getSortingOrder().equals(PageAndSortingObject.SortingOrder.ASC))
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(pageAndSortingObject.getSortingField())));
        else
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(pageAndSortingObject.getSortingField())));

        TypedQuery<OccasionEntity> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(pageAndSortingObject.getPage());
        query.setMaxResults(pageAndSortingObject.getSize());

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
