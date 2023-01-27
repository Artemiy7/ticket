package net.pdfgenerator.enums.ticket;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum TicketType implements Serializable {

    //
    @JsonProperty("TRAIN_INTERCITY")
    TRAIN_INTERCITY("Train Intercity", SeatPlaceType.TRAIN_INTERCITY_ECONOMIC,
                                                      SeatPlaceType.TRAIN_INTERCITY_BUSINESS),

    //ConcertClubOccasionCost
    @JsonProperty("CONCERT_CLUB")
    CONCERT_CLUB("Concert club", SeatPlaceType.CONCERT_CLUB_BALCONY,
                                                           SeatPlaceType.CONCERT_CLUB_FAN,
                                                           SeatPlaceType.CONCERT_CLUB_VIP);

    private static final Map<String, TicketType> ENUM_MAP;
    private String ticketTypeObject;
    private List<SeatPlaceType> seatPlaceTypes;

    TicketType(String ticketTypeObject, SeatPlaceType...seatPlaceTypes) {
        this.ticketTypeObject = ticketTypeObject;
        this.seatPlaceTypes = Arrays.asList(seatPlaceTypes);
    }

    public String getTicketTypeObject() {
        return ticketTypeObject;
    }

    public List<SeatPlaceType> getSeatPlaceTypes() {
        return seatPlaceTypes;
    }

    static {
        Map<String, TicketType> map = new ConcurrentHashMap<>();
        for (TicketType instance : TicketType.values()) {
            map.put(String.valueOf(instance.ticketTypeObject),  instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static TicketType getTicketTypeByString (String seatPlaceType ) {
        return ENUM_MAP.get(seatPlaceType);
    }

    @Override
    public String toString() {
        return ticketTypeObject;
    }
}