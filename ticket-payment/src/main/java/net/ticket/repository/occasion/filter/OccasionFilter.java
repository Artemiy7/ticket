package net.ticket.repository.occasion.filter;

import net.ticket.domain.entity.occasion.OccasionEntity;

import javax.persistence.criteria.*;

@FunctionalInterface
public interface OccasionFilter {
    Predicate filterOccasion(Root<OccasionEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder, Join<OccasionEntity, ?> join, String requestParam) throws IllegalArgumentException;
}
