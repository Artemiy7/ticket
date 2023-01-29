package net.ticket.enums.filtertype;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public enum OccasionFilterType {
    @ApiModelProperty("TICKET_TYPE")
    @JsonProperty("TICKET_TYPE")
    TICKET_TYPE("ticketTypeOccasionFilter"),
    @ApiModelProperty("OCCASION_DATE_FROM")
    @JsonProperty("OCCASION_DATE_FROM")
    OCCASION_DATE_FROM("occasionDateFromOccasionFilter"),
    @ApiModelProperty("OCCASION_DATE_TO")
    @JsonProperty("OCCASION_DATE_TO")
    OCCASION_DATE_TO("occasionDateToOccasionFilter"),
    @ApiModelProperty("OCCASION_NAME")
    @JsonProperty("OCCASION_NAME")
    OCCASION_NAME("occasionNameOccasionFilter"),
    @ApiModelProperty("NOT_BOOKED_SEATS_FROM")
    @JsonProperty("NOT_BOOKED_SEATS_FROM")
    NOT_BOOKED_SEATS_FROM("notBookedSeatsFromOccasionFilter"),
    @ApiModelProperty("NOT_BOOKED_SEATS_TO")
    @JsonProperty("NOT_BOOKED_SEATS_TO")
    NOT_BOOKED_SEATS_TO("notBookedSeatsToOccasionFilter"),
    @ApiModelProperty("NUMBER_OF_SEATS_FROM")
    @JsonProperty("NUMBER_OF_SEATS_FROM")
    NUMBER_OF_SEATS_FROM("numberOfSeatsFromOccasionFilter"),
    @ApiModelProperty("NUMBER_OF_SEATS_TO")
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
