package net.ticket.service.occasion;

import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.repository.occasion.OccasionRepository;
import net.ticket.repository.occasion.filter.OccasionFilter;
import net.ticket.dto.occasion.OccasionDto;
import net.ticket.request.pagination.PaginationRequest;
import net.ticket.entity.occasion.OccasionEntity;
import net.ticket.enums.filtertype.OccasionFilterType;
import net.ticket.service.occasion.cost.OccasionCost;
import net.ticket.ticketexception.occasion.CorruptedOccasionException;
import net.ticket.ticketexception.occasion.CorruptedOccasionSeatException;
import net.ticket.ticketexception.occasion.OccasionOutdatedException;
import net.ticket.transformers.OccasionEntityToOccasionDtoNoSeatsTransformer;
import net.ticket.transformers.OccasionEntityToOccasionDtoTransformer;
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
    public Optional<OccasionDto> findOccasionWithOccasionSeats(long id) throws CorruptedOccasionException, OccasionOutdatedException, CorruptedOccasionSeatException {
        Optional<OccasionEntity> occasionEntityOptional = occasionRepository.getOccasionById(id);
        if (occasionEntityOptional.isEmpty() || occasionEntityOptional.get().getOccasionSeatEntitySet().size() == 0) {
            LOGGER.info("No such OccasionEntity " + id);
            return Optional.empty();
        }
        OccasionEntity occasionEntity = occasionEntityOptional.get();
        checkOccasionEntity(occasionEntity);

        OccasionDto occasionDto = occasionEntityToOccasionDtoTransformer.transform(occasionEntity);
        occasionDto.setDaysToOccasion(calculateDaysToOccasion(occasionEntity.getOccasionTime()));
        occasionDto.setNotBookedSeats(getNotBookedSeats(occasionEntity));

        OccasionCost occasionCost = OccasionCost.getBeanByName(occasionEntity.getTicketType().getTicketTypeObject());

        for (OccasionSeatDto occasionSeatDto : occasionDto.getOccasionSeatDto()) {
            occasionCost.calculateTicketCost(occasionSeatDto, occasionEntity.getInitialCost());
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

    private void checkOccasionEntity(OccasionEntity occasionEntity) throws OccasionOutdatedException, CorruptedOccasionException {
        if (LocalDateTime.now().isAfter(occasionEntity.getOccasionTime())) {
            LOGGER.error("Occasion is before current date " + occasionEntity.getOccasionId());
            throw new OccasionOutdatedException(occasionEntity.getOccasionName().concat(" is before current date"));
        }
        if (occasionEntity.getNumberOfSeats() != occasionEntity.getOccasionSeatEntitySet().size()) {
            LOGGER.error("Occasion is corrupted " + occasionEntity.getOccasionId());
            throw new CorruptedOccasionException(occasionEntity.getOccasionName().concat(" is corrupted"));
        }
    }

    @Transactional(readOnly = true)
    public Optional<List<OccasionDto>> findFilteredOccasions(Map<String, List<String>> filterMap, PaginationRequest paginationRequest) throws RuntimeException {
        Map<OccasionFilter, List<String>> occasionFilterMap = new HashMap<>();

        for (Map.Entry<String,List<String>> entry : filterMap.entrySet()) {
            OccasionFilter occasionFilter = OccasionFilter.getBeanByName(OccasionFilterType.valueOf(entry.getKey()).getFilterType());
            if (occasionFilter.getIsRange() && entry.getValue().size() > 1)
                throw new RuntimeException("Double same type FROM/TO request");
            occasionFilterMap.put(occasionFilter, entry.getValue());
        }

        Optional<List<OccasionEntity>> occasionEntityListOptional = occasionRepository.findOccasionsByFilter(occasionFilterMap, paginationRequest.getSize(), paginationRequest.getResultOrder());

        if (occasionEntityListOptional.isEmpty()) {
            LOGGER.info("No OccasionEntity was found by filter");
            return Optional.empty();
        }

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
}
