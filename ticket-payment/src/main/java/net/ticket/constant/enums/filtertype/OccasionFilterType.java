package net.ticket.constant.enums.filtertype;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public enum OccasionFilterType {
    @JsonProperty("TICKET_TYPE")
    TICKET_TYPE("ticketTypeOccasionFilter"),
    @JsonProperty("OCCASION_DATE_FROM")
    OCCASION_DATE_FROM("occasionDateFromOccasionFilter"),
    @JsonProperty("OCCASION_DATE_TO")
    OCCASION_DATE_TO("occasionDateToOccasionFilter"),
    @JsonProperty("OCCASION_NAME")
    OCCASION_NAME("occasionNameOccasionFilter"),
    @JsonProperty("NOT_BOOKED_SEATS_FROM")
    NOT_BOOKED_SEATS_FROM("notBookedSeatsFromOccasionFilter"),
    @JsonProperty("NOT_BOOKED_SEATS_TO")
    NOT_BOOKED_SEATS_TO("notBookedSeatsToOccasionFilter"),
    @JsonProperty("NUMBER_OF_SEATS_FROM")
    NUMBER_OF_SEATS_FROM("numberOfSeatsFromOccasionFilter"),
    @JsonProperty("NUMBER_OF_SEATS_TO")
    NUMBER_OF_SEATS_TO("numberOfSeatsToOccasionFilter");

    public static final List<OccasionFilterType> OCCASION_FILTER_TYPE_LIST;
    private String filterType;

    OccasionFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterType() {
        return filterType;
    }

    public static List<OccasionFilterType> getOccasionFilterTypeList() {
        return OCCASION_FILTER_TYPE_LIST;
    }

    static {
        OCCASION_FILTER_TYPE_LIST = List.of(OccasionFilterType.values());
    }

    @Override
    public String toString() {
        return name();
    }
}
