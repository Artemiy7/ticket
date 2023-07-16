package net.ticket.constant.enums.search.occasion;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.ticket.constant.enums.ticket.TicketType;
import net.ticket.domain.entity.occasion.OccasionEntity;
import net.ticket.repository.occasion.filter.OccasionFilter;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum OccasionQueryParameterOperation implements OccasionFilter {
    @JsonProperty("ticket_type")
    TICKET_TYPE("ticket_type", false, (root, query, builder, join, requestParam) -> {
        return builder.equal(root.get("ticketType"), TicketType.valueOf(requestParam));
    }),

    @JsonProperty("occasion_name")
    OCCASION_NAME("occasion_name", false, (root, query, builder, join, requestParam) -> {
        return builder.equal(root.get("occasionName"), requestParam);
    }),

    @JsonProperty("occasion_name")
    OCCASION_ADDRESS("occasion_address", false, (root, query, builder, join, requestParam) -> {
        return builder.equal(root.get("occasionAddress"), requestParam);
    }),


    @JsonProperty("occasion_seats_from")
    OCCASION_SEATS_FROM("occasion_seats_from", true, (root, query, builder, join, requestParam) -> {
        return builder.greaterThanOrEqualTo(root.get("numberOfSeats"), Integer.valueOf(requestParam));
    }),

    @JsonProperty("occasion_seats_to")
    OCCASION_SEATS_TO("occasion_seats_to", true, (root, query, builder, join, requestParam) -> {
        return builder.lessThanOrEqualTo(root.get("numberOfSeats"), Integer.valueOf(requestParam));
    }),

    @JsonProperty("occasion_date_from")
    OCCASION_DATE_FROM("occasion_date_from", true, (root, query, builder, join, requestParam) -> {
        return builder.greaterThanOrEqualTo(root.get("occasionTime"), LocalDate.parse(requestParam).atStartOfDay());
    }),

    @JsonProperty("occasion_date_to")
    OCCASION_DATE_TO("occasion_date_to", true, (root, query, builder, join, requestParam) -> {
        return builder.lessThanOrEqualTo(root.get("occasionTime"), LocalDate.parse(requestParam).atStartOfDay());
    }),

    @JsonProperty("size")
    SIZE("size", true, (root, query, builder, join, requestParam) -> {
        return builder.conjunction();
    }),

    @JsonProperty("page")
    PAGE("page", true, (root, query, builder, join, requestParam) -> {
        return builder.conjunction();
    }),

    @JsonProperty("sort_field")
    SORT_FIELD("sort_field", true, (root, query, builder, join, requestParam) -> {
        return builder.conjunction();
    }),

    @JsonProperty("sort_order")
    SORT_ORDER("sort_order", true, (root, query, builder, join, requestParam) -> {
        return builder.conjunction();
    });


//    @JsonProperty("not_booked_seats_from")
//    NOT_BOOKED_SEATS_FROM("not_booked_seats_from", true, (root, query, builder, join, requestParam) -> {
//        return builder.ge(root.get("numberOfSeats"), Integer.valueOf(requestParam));
//    }),
//
//    @JsonProperty("not-booked-seats-to")
//    NOT_BOOKED_SEATS_TO("not_booked_seats_to", true, (root, query, builder, join, requestParam) -> {
//        Join<OccasionEntity, OccasionSeatEntity> join2 = root.join(OccasionEntity., JoinType.INNER);
//        return builder.le(builder.countDistinct(join2.get("isBooked")), Integer.valueOf(requestParam));
//    });

    private static final Map<String, OccasionQueryParameterOperation> ENUM_MAP;
    private static final List<OccasionQueryParameterOperation> OCCASION_FILTER_TYPE_LIST;
    private String queryParameter;
    private boolean isSingle;
    private OccasionFilter occasionFilter;

    OccasionQueryParameterOperation(String queryParameter, boolean isSingle, OccasionFilter occasionFilter) {
        this.queryParameter = queryParameter;
        this.isSingle = isSingle;
        this.occasionFilter = occasionFilter;
    }

    static {
        Map<String, OccasionQueryParameterOperation> map = new ConcurrentHashMap<>();
        for (OccasionQueryParameterOperation instance : OccasionQueryParameterOperation.values()) {
            map.put(String.valueOf(instance.queryParameter),  instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static OccasionQueryParameterOperation getOccasionQueryParameterOperation (String operation ) {
        return ENUM_MAP.get(operation);
    }

    public String getQueryParameterType() {
        return queryParameter;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public static List<OccasionQueryParameterOperation> getOccasionFilterTypeList() {
        return OCCASION_FILTER_TYPE_LIST;
    }

    static {
        OCCASION_FILTER_TYPE_LIST = List.of(OccasionQueryParameterOperation.values());
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public Predicate filterOccasion(Root<OccasionEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder, Join<OccasionEntity, ?> join, String requestParam) throws IllegalArgumentException {
        return occasionFilter.filterOccasion(root, query, builder, join, requestParam);
    }
}
