package net.ticket.repository.occasion.filter;

import net.ticket.entity.occasion.OccasionEntity;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
class OccasionNameOccasionFilter implements OccasionFilter {
    @Override
    public Predicate filterOccasion(Root<OccasionEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder, String requestParam) {
        return builder.equal(root.get("occasionName"), requestParam);
    }

    @Override
    public boolean getIsRange() {
        return isRange;
    }
}
