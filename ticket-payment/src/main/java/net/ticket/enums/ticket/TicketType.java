package net.ticket.enums.ticket;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum TicketType implements Serializable {

    //net.ticket.service.occasion.cost.train.IntercityTrainCost
    @JsonProperty("TRAIN_INTERCITY")
    TRAIN_INTERCITY("intercityTrainCost", SeatPlaceType.TRAIN_INTERCITY_ECONOMIC,
                                                        SeatPlaceType.TRAIN_INTERCITY_BUSINESS),
    //net.ticket.service.occasion.cost.concert.ConcertClubOccasionCost
    @JsonProperty("CONCERT_CLUB")
    CONCERT_CLUB("concertClubOccasionCost", SeatPlaceType.CONCERT_CLUB_BALCONY,
                                                          SeatPlaceType.CONCERT_CLUB_FAN,
                                                          SeatPlaceType.CONCERT_CLUB_VIP),
    //net.ticket.service.occasion.cost.concert.ConcertStadionOccasionCost
    @JsonProperty("CONCERT_STADION")
    CONCERT_STADION("concertStadionOccasionCost", SeatPlaceType.CONCERT_STADION_VIP,
                                                                SeatPlaceType.CONCERT_STADION_FAN,
                                                                SeatPlaceType.CONCERT_STADION_SEAT);

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