package net.ticket.entity.occasion;

import lombok.*;
import net.ticket.enums.ticket.TicketType;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "OccasionId")
    private long occasionId;

    @Column(name = "OccasionName")
    @NonNull
    private String occasionName;

    @Column(name = "OccasionTime")
    @NonNull
    private LocalDateTime occasionTime;

    @Column(name = "NumberOfSeats")
    @NonNull
    private short numberOfSeats;

    @Enumerated(EnumType.STRING)
    @Column(name = "TicketType")
    @NonNull
    private TicketType ticketType;

    @Column(name = "OccasionAddress")
    @NonNull
    private String occasionAddress;

    @Column(name = "IsActive")
    @NonNull
    private boolean isActive;

    @Column(name = "InitialCost")
    @NonNull
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
