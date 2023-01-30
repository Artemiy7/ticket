package net.ticket.enums.ticket;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum SeatPlaceType {

    //net.ticket.service.occasion.cost.concert.ConcertClubOccasionCost
    @JsonProperty("CONCERT_CLUB_VIP")
    CONCERT_CLUB_VIP("concertClubVip"),
    @JsonProperty("CONCERT_CLUB_FAN")
    CONCERT_CLUB_FAN("concertClubFan"),
    @JsonProperty("CONCERT_CLUB_BALCONY")
    CONCERT_CLUB_BALCONY("concertClubBalcony"),

    //net.ticket.service.occasion.cost.concert.ConcertStadionOccasionCost
    @JsonProperty("CONCERT_STADION_VIP")
    CONCERT_STADION_VIP("concertStadionVip"),
    @JsonProperty("CONCERT_STADION_FAN")
    CONCERT_STADION_FAN("concertStadionFan"),
    @JsonProperty("CONCERT_STADION_SEAT")
    CONCERT_STADION_SEAT("concertStadionSeat"),

    //net.ticket.service.occasion.cost.train.IntercityTrainCost
    @JsonProperty("TRAIN_INTERCITY_BUSINESS")
    TRAIN_INTERCITY_BUSINESS("trainIntercityBusiness"),
    @JsonProperty("TRAIN_INTERCITY_ECONOMIC")
    TRAIN_INTERCITY_ECONOMIC("trainIntercityEconomic");

    private static final Map<String, SeatPlaceType> ENUM_MAP;
    private String seatPlaceType;

    SeatPlaceType(String seatPlaceType) {
        this.seatPlaceType = seatPlaceType;
    }

    public String getSeatPlaceType() {
        return seatPlaceType;
    }

    static {
        Map<String, SeatPlaceType> map = new ConcurrentHashMap<>();
        for (SeatPlaceType instance : SeatPlaceType.values()) {
            map.put(String.valueOf(instance.seatPlaceType),  instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static SeatPlaceType getSeatPlaceTypeByString (String seatPlaceType ) {
        return ENUM_MAP.get(seatPlaceType);
    }

    @Override
    public String toString() {
        return  name();
    }
}
