package net.pdfgenerator.enums.ticket;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum SeatPlaceType {

    @JsonProperty("CONCERT_CLUB_VIP")
    CONCERT_CLUB_VIP("concert club vip"),
    @JsonProperty("CONCERT_CLUB_FAN")
    CONCERT_CLUB_FAN("Concert club fan"),
    @JsonProperty("CONCERT_CLUB_BALCONY")
    CONCERT_CLUB_BALCONY("Concert club balcony"),

    @JsonProperty("CONCERT_STADION_VIP")
    CONCERT_STADION_VIP("Concert stadion vip"),
    @JsonProperty("CONCERT_STADION_FAN")
    CONCERT_STADION_FAN("Concert stadion fan"),
    @JsonProperty("CONCERT_STADION_SEAT")
    CONCERT_STADION_SEAT("Concert stadion seat"),

    @JsonProperty("TRAIN_INTERCITY_BUSINESS")
    TRAIN_INTERCITY_BUSINESS("Train intercity business"),
    @JsonProperty("TRAIN_INTERCITY_ECONOMIC")
    TRAIN_INTERCITY_ECONOMIC("Train intercity economic");

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
        return  seatPlaceType;
    }
}
