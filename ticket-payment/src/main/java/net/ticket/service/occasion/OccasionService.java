package net.ticket.service.occasion;

import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.repository.occasion.OccasionRepository;
import net.ticket.repository.occasion.filter.OccasionFilter;
import net.ticket.dto.occasion.OccasionDto;
import net.ticket.request.pagination.PaginationRequest;
import net.ticket.entity.occasion.OccasionEntity;
import net.ticket.constant.enums.filtertype.OccasionFilterType;
import net.ticket.service.occasion.cost.OccasionCost;
import net.ticket.ticketexception.occasion.*;
import net.ticket.transformer.occasion.OccasionEntityToOccasionDtoNoSeatsTransformer;
import net.ticket.transformer.occasion.OccasionEntityToOccasionDtoTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OccasionService {
    private final static Logger LOGGER = LoggerFactory.getLogger(OccasionService.class);
    private final OccasionEntityToOccasionDtoTransformer occasionEntityToOccasionDtoTransformer;
    private final OccasionEntityToOccasionDtoNoSeatsTransformer occasionEntityToOccasionDtoNoSeatsTransformer;
    private final OccasionRepository occasionRepository;

    @Autowired
    public OccasionService(OccasionEntityToOccasionDtoTransformer occasionEntityToOccasionDtoTransformer,
                           OccasionEntityToOccasionDtoNoSeatsTransformer occasionEntityToOccasionDtoNoSeatsTransformer,
                           OccasionRepository occasionRepository) {
        this.occasionEntityToOccasionDtoTransformer = occasionEntityToOccasionDtoTransformer;
        this.occasionEntityToOccasionDtoNoSeatsTransformer = occasionEntityToOccasionDtoNoSeatsTransformer;
        this.occasionRepository = occasionRepository;
    }

    @Transactional(readOnly = true)
    public Optional<OccasionDto> findOccasionWithOccasionSeats(long id) throws CorruptedOccasionException, OccasionOutdatedException, CorruptedOccasionSeatException, NoSuchOccasionException {
        Optional<OccasionEntity> occasionEntityOptional = occasionRepository.getOccasionById(id);
        if (occasionEntityOptional.isEmpty() || occasionEntityOptional.get().getOccasionSeatEntitySet().size() == 0) {
            LOGGER.info("No such OccasionEntity " + id);
            throw new NoSuchOccasionException("No such OccasionEntity " + id);
        }

        OccasionEntity occasionEntity = occasionEntityOptional.get();
        checkOccasionEntityTime(occasionEntity);
        checkOccasionEntityNumberOfSeats(occasionEntity);
        checkOccasionEntityTicketTypes(occasionEntity);

        OccasionDto occasionDto = occasionEntityToOccasionDtoTransformer.transform(occasionEntity);
        occasionDto.setDaysToOccasion(calculateDaysToOccasion(occasionEntity.getOccasionTime()));
        occasionDto.setNotBookedSeats(getNotBookedSeats(occasionEntity));

        OccasionCost occasionCost = OccasionCost.getBeanByName(occasionEntity.getTicketType().getTicketTypeObject());

        for (OccasionSeatDto occasionSeatDto : occasionDto.getOccasionSeatDto()) {
            occasionSeatDto.setCost(occasionCost.calculateTicketCost(occasionSeatDto, occasionEntity.getInitialCost()));
        }
        return Optional.of(occasionDto);
    }

    private short calculateDaysToOccasion(LocalDateTime occasionDateTime) {
        return (short) ChronoUnit.DAYS.between(LocalDateTime.now(), occasionDateTime.plusDays(1));
    }

    /**
     * Not performance-safe.
     * Only if you have already fetched OccasionSeatEntitySet.
     */
    private short getNotBookedSeats(OccasionEntity occasionEntity) {
        return (short) occasionEntity.getOccasionSeatEntitySet()
                .stream()
                .filter(seat -> !seat.isBooked())
                .count();
    }

    private void checkOccasionEntityTime(OccasionEntity occasionEntity) throws OccasionOutdatedException {
        if (LocalDateTime.now().isAfter(occasionEntity.getOccasionTime().plusMinutes(30))) {
            LOGGER.error("Occasion is before current date " + occasionEntity.getOccasionId());
            throw new OccasionOutdatedException(occasionEntity.getOccasionName().concat(" is before current date"));
        }
    }

    private void checkOccasionEntityNumberOfSeats(OccasionEntity occasionEntity) throws CorruptedOccasionException {
        if (occasionEntity.getNumberOfSeats() != occasionEntity.getOccasionSeatEntitySet().size()) {
            LOGGER.error("Occasion is corrupted " + occasionEntity.getOccasionId());
            throw new CorruptedOccasionException(occasionEntity.getOccasionName().concat(" is corrupted"));
        }
    }

    private void checkOccasionEntityTicketTypes(OccasionEntity occasionEntity) throws CorruptedOccasionException {
        occasionEntity.getOccasionSeatEntitySet().forEach(occasionSeatEntity -> {
            if (!occasionEntity.getTicketType().getSeatPlaceTypes().contains(occasionSeatEntity.getSeatPlaceType())) {
                LOGGER.error("No such SeatPlaceType " + occasionSeatEntity.getSeatPlaceType() + " in this TicketType " + occasionSeatEntity.getOccasionEntity().getTicketType());
                throw new CorruptedOccasionException("No such SeatPlaceType " + occasionSeatEntity.getSeatPlaceType() + " in this TicketType " + occasionSeatEntity.getOccasionEntity().getTicketType());
            }
        });
    }

    @Transactional(readOnly = true)
    public Optional<List<OccasionDto>> findFilteredOccasions(Map<String, List<String>> filterMap, PaginationRequest paginationRequest) throws RuntimeException {
        Map<OccasionFilter, List<String>> occasionFilterMap = new HashMap<>();

        for (Map.Entry<String,List<String>> entry : filterMap.entrySet()) {
            OccasionFilter occasionFilter = OccasionFilter.getBeanByName(OccasionFilterType.valueOf(entry.getKey()).getFilterType());
            if (occasionFilter.getIsRange() && entry.getValue().size() > 1) {
                LOGGER.error("Double same type FROM/TO request ");
                throw new OccasionFilterException("Double same type FROM/TO request");
            }
            occasionFilterMap.put(occasionFilter, entry.getValue());
        }

        Optional<List<OccasionEntity>> occasionEntityListOptional = occasionRepository.findOccasionsByFilter(occasionFilterMap, paginationRequest.getSize(), paginationRequest.getResultOrder());

        if (occasionEntityListOptional.isEmpty() || occasionEntityListOptional.get().isEmpty()) {
            LOGGER.error("No OccasionEntity was found by filter");
            throw new NoSuchOccasionException("No such OccasionEntity");
        }

        occasionEntityListOptional.get().forEach(occasionEntity -> {
            checkOccasionEntityNumberOfSeats(occasionEntity);
            checkOccasionEntityTicketTypes(occasionEntity);
            if (!paginationRequest.isWithOutdated())
                checkOccasionEntityTime(occasionEntity);
        });

        if (paginationRequest.getSortingOrder() == PaginationRequest.SortingOrder.DESC)
            Collections.sort(occasionEntityListOptional.get(), Collections.reverseOrder());
        else
            Collections.sort(occasionEntityListOptional.get());

        return occasionEntityListOptional.map(occasionEntityList -> occasionEntityList
                        .stream()
                        .map(occasionEntity ->
                            occasionEntityToOccasionDtoNoSeatsTransformer.transform(occasionEntity)
                                                                         .setNotBookedSeats(occasionRepository.findNotBookedSeatForOccasion(occasionEntity))
                                                                         .setDaysToOccasion(calculateDaysToOccasion(occasionEntity.getOccasionTime())))
                        .collect(Collectors.toList()));
    }

    @Transactional
    public void saveOccasionList(List<OccasionEntity> occasionEntityList) {
        for (OccasionEntity occasionEntity : occasionEntityList) {
            if (occasionRepository.findOccasionByNameAndDateAndAddress(occasionEntity).isEmpty()) {
                occasionRepository.persistOccasion(occasionEntity);
                LOGGER.info("Occasion saved " + occasionEntity.getOccasionName() + " " + occasionEntity.getOccasionAddress() +
                        " " + occasionEntity.getOccasionTime() + " " + occasionEntity.getTicketType());
            } else {
                LOGGER.info("Occasion already exist in database" + occasionEntity.getOccasionName() + " " + occasionEntity.getOccasionAddress() +
                        " " + occasionEntity.getOccasionTime() + " " + occasionEntity.getTicketType());
            }
        }
    }

    @Transactional
    public void setOutdatedOccasionNotActiveByCurrentDate() {
        occasionRepository.setOutdatedOccasionNotActive(LocalDateTime.now());
    }
}
