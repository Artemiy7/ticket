package net.ticket.entity.occasion;

import lombok.*;
import net.ticket.constant.enums.ticket.TicketType;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "Occasion")
public class OccasionEntity implements Comparable<OccasionEntity>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OccasionId")
    private long occasionId;

    @Column(name = "OccasionName", nullable = false)
    private String occasionName;

    @Column(name = "OccasionTime", nullable = false)
    private LocalDateTime occasionTime;

    @Column(name = "NumberOfSeats", nullable = false)
    private short numberOfSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "TicketType", nullable = false)
    private TicketType ticketType;

    @Column(name = "OccasionAddress", nullable = false)
    private String occasionAddress;

    @Column(name = "IsActive", nullable = false)
    private boolean isActive;

    @Column(name = "InitialCost", nullable = false)
    private BigDecimal initialCost;

    @OneToMany(mappedBy = "occasionEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @NonNull
    private Set<OccasionSeatEntity> occasionSeatEntitySet;


    public LocalDateTime getOccasionTime() {
        return occasionTime;
    }

    @Override
    public int compareTo(OccasionEntity occasionEntity) {
        LocalDateTime ldt1 = getOccasionTime();
        LocalDateTime ldt2 = occasionEntity.getOccasionTime();

        int diff = ldt1.compareTo(ldt2);
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
