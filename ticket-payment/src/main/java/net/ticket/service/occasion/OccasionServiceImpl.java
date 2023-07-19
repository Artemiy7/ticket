package net.ticket.service.occasion;

import net.ticket.domain.pagination.PageAndSortingObject;
import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.repository.occasion.OccasionRepository;
import net.ticket.dto.occasion.OccasionDto;
import net.ticket.domain.entity.occasion.OccasionEntity;
import net.ticket.constant.enums.search.OccasionQueryParameterOperation;
import net.ticket.repository.occasion.OccasionSeatRepository;
import net.ticket.service.occasion.cost.OccasionCost;
import net.ticket.ticketexception.occasion.*;
import net.ticket.transformer.occasion.OccasionEntityToOccasionDtoNoSeatsTransformer;
import net.ticket.transformer.occasion.OccasionEntityToOccasionDtoTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OccasionServiceImpl implements OccasionService {
    private final static Logger LOGGER = LoggerFactory.getLogger(OccasionServiceImpl.class);
    private final OccasionEntityToOccasionDtoTransformer occasionEntityToOccasionDtoTransformer;
    private final OccasionEntityToOccasionDtoNoSeatsTransformer occasionEntityToOccasionDtoNoSeatsTransformer;
    private final OccasionRepository occasionRepository;
    private final OccasionSeatRepository occasionSeatRepository;

    public OccasionServiceImpl(OccasionEntityToOccasionDtoTransformer occasionEntityToOccasionDtoTransformer,
                               OccasionEntityToOccasionDtoNoSeatsTransformer occasionEntityToOccasionDtoNoSeatsTransformer,
                               OccasionRepository occasionRepository,
                               OccasionSeatRepository occasionSeatRepository) {
        this.occasionEntityToOccasionDtoTransformer = occasionEntityToOccasionDtoTransformer;
        this.occasionEntityToOccasionDtoNoSeatsTransformer = occasionEntityToOccasionDtoNoSeatsTransformer;
        this.occasionRepository = occasionRepository;
        this.occasionSeatRepository = occasionSeatRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<OccasionDto> findOccasionWithOccasionSeats(long id) {
        Optional<OccasionEntity> occasionEntityOptional = occasionRepository.getOccasionById(id);
        if (occasionEntityOptional.isEmpty() || occasionEntityOptional.get().getOccasionSeatEntitySet().size() == 0) {
            LOGGER.info("No such OccasionEntity " + id);
            return Optional.empty();
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
    private int getNotBookedSeats(OccasionEntity occasionEntity) {
        return (int) occasionEntity.getOccasionSeatEntitySet()
                .stream()
                .filter(seat -> !seat.isBooked())
                .count();
    }

    private void checkOccasionEntityTime(OccasionEntity occasionEntity) throws OccasionOutdatedException {
        if (LocalDateTime.now().isAfter(occasionEntity.getOccasionTime().plusMinutes(30))) {
            LOGGER.error("Occasion is before current date " + occasionEntity.getOccasionId());
            throw new OccasionOutdatedException(occasionEntity.getOccasionName() + " is before current date");
        }
    }

    private void checkOccasionEntityNumberOfSeats(OccasionEntity occasionEntity) throws CorruptedOccasionException {
        if (occasionEntity.getNumberOfSeats() != occasionEntity.getOccasionSeatEntitySet().size()) {
            LOGGER.error("Occasion is corrupted " + occasionEntity.getOccasionId());
            throw new CorruptedOccasionException(occasionEntity.getOccasionName() + " is corrupted");
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
    @Override
    public Optional<List<OccasionDto>> findOccasionsByParameters(MultiValueMap<String, String> searchMap, PageAndSortingObject pageAndSortingObject) {
        Map<OccasionQueryParameterOperation, List<String>> occasionParameterMap = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : searchMap.entrySet()) {
            OccasionQueryParameterOperation parameterOperation = OccasionQueryParameterOperation.getOccasionQueryParameterOperation(entry.getKey());
            if (parameterOperation == null) {
                LOGGER.error("No such parameter " + entry.getKey());
                throw new OccasionFilterException("No such parameter " + entry.getKey());
            }
            if (parameterOperation.isSingle() && entry.getValue().size() > 1) {
                LOGGER.error("Double type for " + parameterOperation.getQueryParameterType());
                throw new OccasionFilterException("Double type for " + parameterOperation.getQueryParameterType());
            }
            occasionParameterMap.put(parameterOperation, entry.getValue());
        }

        Optional<List<OccasionEntity>> occasionEntityListOptional = occasionRepository.findOccasionsByParametersMap(occasionParameterMap, pageAndSortingObject);

        if (occasionEntityListOptional.isEmpty()) {
            LOGGER.info("No OccasionEntity was found");
            return Optional.empty();
        }

        occasionEntityListOptional.get().forEach(occasionEntity -> {
            checkOccasionEntityNumberOfSeats(occasionEntity);
            checkOccasionEntityTicketTypes(occasionEntity);
        });

        return occasionEntityListOptional.map(occasionEntityList -> occasionEntityList
                        .stream()
                        .map(occasionEntity ->
                            occasionEntityToOccasionDtoNoSeatsTransformer.transform(occasionEntity)
                                    .setNotBookedSeats(occasionSeatRepository.countNotBookedOccasionSeats(occasionEntity))
                                    .setDaysToOccasion(calculateDaysToOccasion(occasionEntity.getOccasionTime())))
                        .collect(Collectors.toList()));
    }

    @Transactional
    @Override
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
    @Override
    public void setOutdatedOccasionNotActiveByCurrentDate() {
        occasionRepository.updateOutdatedOccasionSetNotActive(LocalDateTime.now());
    }
}
