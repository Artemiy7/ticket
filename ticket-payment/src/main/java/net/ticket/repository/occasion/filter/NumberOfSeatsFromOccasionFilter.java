package net.ticket.repository.occasion.filter;

import net.ticket.entity.occasion.OccasionEntity;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
class NumberOfSeatsFromOccasionFilter implements OccasionFilter {
    public final boolean isRange = true;
    @Override
    public Predicate filterOccasion(Root<OccasionEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder, String requestParam) {
        return builder.ge(root.get("numberOfSeats"), Integer.valueOf(requestParam));
    }

    @Override
    public boolean getIsRange() {
        return false;
    }
}
