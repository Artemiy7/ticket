package net.ticket.repository.occasion.filter;

import net.ticket.entity.occasion.OccasionEntity;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;

@Component
class OccasionDateToOccasionFilter implements OccasionFilter {
    public final boolean isRange = true;
    @Override
    public Predicate filterOccasion(Root<OccasionEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder, String requestParam) {
        return builder.lessThanOrEqualTo(root.get("occasionTime"), LocalDate.parse(requestParam).atStartOfDay());
    }

    @Override
    public boolean getIsRange() {
        return isRange;
    }
}
