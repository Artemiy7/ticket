package net.ticket.config.occasion.cost.concert;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "service.occasions.occasion-coefficient.concert.club")
public class ConcertClubOccasionCostConfig {
    private BigDecimal vip;
    private BigDecimal fan;
    private BigDecimal balcony;
    private BigDecimal date;
    private BigDecimal seat;
}
