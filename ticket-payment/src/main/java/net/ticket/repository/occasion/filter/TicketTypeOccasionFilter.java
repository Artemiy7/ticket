package net.ticket.repository.occasion.filter;

import net.ticket.entity.occasion.OccasionEntity;
import net.ticket.constant.enums.ticket.TicketType;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
class TicketTypeOccasionFilter implements OccasionFilter {
    //public final boolean isRange = false;
    @Override
    public Predicate filterOccasion(Root<OccasionEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder, String requestParam) throws IllegalArgumentException {
        return builder.equal(root.get("ticketType"), TicketType.valueOf(requestParam));
    }

    @Override
    public boolean getIsRange() {
        return isRange;
    }
}
