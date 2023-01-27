package net.ticket.repository.occasion.filter;

import net.ticket.ApplicationContextProvider;
import net.ticket.entity.occasion.OccasionEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface OccasionFilter {
    public final boolean isRange = false;
    static OccasionFilter getBeanByName(String classType) {
        return ApplicationContextProvider.getApplicationContext().getBean(classType, OccasionFilter.class);
    }
    Predicate filterOccasion(Root<OccasionEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder, String requestParam) throws IllegalArgumentException;
    boolean getIsRange();
}
