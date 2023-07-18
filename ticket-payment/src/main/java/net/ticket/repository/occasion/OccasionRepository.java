package net.ticket.repository.occasion;

import net.ticket.constant.OccasionNamedQueriesConstants;
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
    private EntityManager entityManager;

    public Optional<OccasionEntity> getOccasionById(long id) {
        return Optional.ofNullable(entityManager.find(OccasionEntity.class, id));
    }

    public Optional<List<OccasionEntity>> findOccasionsByParametersMap(Map<OccasionQueryParameterOperation, List<String>> occasionSearchMap, PageAndSortingObject pageAndSortingObject) throws IllegalArgumentException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OccasionEntity> criteriaQuery = criteriaBuilder.createQuery(OccasionEntity.class);
        Root<OccasionEntity> root = criteriaQuery.from(OccasionEntity.class);
        List<Predicate> searchRestrictions = new ArrayList<>();

        for (Map.Entry<OccasionQueryParameterOperation, List<String>> entry : occasionSearchMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                List<Predicate> sameTypePredicatesList = new ArrayList<>();
                entry.getValue().forEach(value -> {
                    sameTypePredicatesList.add(entry.getKey().filterOccasion(root, criteriaQuery, criteriaBuilder, value));
                });
                searchRestrictions.add(criteriaBuilder.or(sameTypePredicatesList.toArray(new Predicate[sameTypePredicatesList.size()])));
            } else {
                searchRestrictions.add(criteriaBuilder.and(entry.getKey().filterOccasion(root, criteriaQuery, criteriaBuilder, entry.getValue().get(0))));
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
        TypedQuery<OccasionEntity> query = entityManager.createNamedQuery(OccasionNamedQueriesConstants.findByNameAndDateAndAddress, OccasionEntity.class);
        query.setParameter("OccasionName", ticketOrderDto.getOccasionName());
        query.setParameter("OccasionTime", LocalDateTime.parse(ticketOrderDto.getOccasionTime().toString()));
        query.setParameter("TicketType", ticketOrderDto.getTicketType());
        query.setParameter("OccasionAddress", ticketOrderDto.getOccasionAddress());
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<OccasionEntity> findOccasionByNameAndDateAndAddress(OccasionEntity occasionEntity) {
        TypedQuery<OccasionEntity> query = entityManager.createNamedQuery(OccasionNamedQueriesConstants.findByNameAndDateAndAddress, OccasionEntity.class);
        query.setParameter("OccasionName", occasionEntity.getOccasionName());
        query.setParameter("OccasionTime", LocalDateTime.parse(occasionEntity.getOccasionTime().toString()));
        query.setParameter("TicketType", occasionEntity.getTicketType());
        query.setParameter("OccasionAddress", occasionEntity.getOccasionAddress());
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void persistOccasion(OccasionEntity occasionEntity) {
        entityManager.persist(occasionEntity);
    }

    public void updateOutdatedOccasionSetNotActive(LocalDateTime localDateTime) {
        Query query = entityManager.createNamedQuery(OccasionNamedQueriesConstants.updateOutdatedOccasionSetNotActive);
        query.setParameter("OccasionTime", localDateTime);
        query.executeUpdate();
    }
}
