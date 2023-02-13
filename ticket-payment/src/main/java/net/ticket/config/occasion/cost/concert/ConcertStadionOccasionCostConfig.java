package net.ticket.config.occasion.cost.concert;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "service.occasions.occasion-coefficient.concert.stadion")
public class ConcertStadionOccasionCostConfig {
    private BigDecimal vip;
    private BigDecimal fan;
    private BigDecimal seating;
    private BigDecimal date;
    private BigDecimal seat;
}
