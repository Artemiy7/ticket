package net.ticket.initialdataloader.occasion.loaders.train;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.dto.occasion.occasionloader.OccasionLoaderDto;
import net.ticket.entity.occasion.OccasionEntity;
import net.ticket.enums.occasionloader.OccasionLoader;
import net.ticket.enums.ticket.TicketType;
import net.ticket.initialdataloader.InitialLoader;
import net.ticket.transformer.occasion.OccasionLoaderDtoToOccasionEntityTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class IntercityTrainInitialLoader implements InitialLoader<OccasionEntity> {
    private ObjectMapper initialLoaderObjectMapper;
    private final OccasionLoaderDtoToOccasionEntityTransformer occasionLoaderDtoToOccasionEntityTransformer;
    private final static Logger LOGGER = LoggerFactory.getLogger(IntercityTrainInitialLoader.class);
    private final String filePath = OccasionLoader.TRAIN_INTERCITY_FILE.getFilePath();

    public IntercityTrainInitialLoader(ObjectMapper initialLoaderObjectMapper,
                                       OccasionLoaderDtoToOccasionEntityTransformer occasionLoaderDtoToOccasionEntityTransformer) {
        this.initialLoaderObjectMapper = initialLoaderObjectMapper;
        this.occasionLoaderDtoToOccasionEntityTransformer = occasionLoaderDtoToOccasionEntityTransformer;
    }


    @Override
    public Optional<List<OccasionEntity>> loadInitialDataFromJson() {
        try {
            List<OccasionLoaderDto> occasionLoaderDtoList = Arrays.asList(initialLoaderObjectMapper.readValue(new File(filePath), OccasionLoaderDto[].class));

            List<OccasionEntity> occasionEntities = new ArrayList<>();
            occasionLoaderDtoList.forEach(occasionLoaderDto -> {
                if (LocalDateTime.now().isAfter(occasionLoaderDto.getOccasionTime().plusMinutes(30))) {
                    LOGGER.info(occasionLoaderDto.getOccasionName() + " " + occasionLoaderDto.getOccasionAddress()
                            + " " + occasionLoaderDto.getOccasionTime() + " " + occasionLoaderDto.getTicketType()
                            + " is outdated");
                    return;
                } else if (!occasionLoaderDto.getTicketType().equals(TicketType.TRAIN_INTERCITY)) {
                    LOGGER.info(occasionLoaderDto.getOccasionName() + " " + occasionLoaderDto.getOccasionAddress()
                            + " " + occasionLoaderDto.getOccasionTime() + " " + occasionLoaderDto.getTicketType()
                            + " TicketType is wrong");
                    return;
                }
                Set<OccasionSeatDto> occasionSeatDtoSet = new HashSet<>();
                occasionLoaderDto.getOccasionSeatLoaderDtoSet().forEach(occasionSeatLoaderDto -> {
                    for (short seatNumber = occasionSeatLoaderDto.getFrom(); seatNumber <= occasionSeatLoaderDto.getTo(); seatNumber++) {
                        occasionSeatDtoSet.add(new OccasionSeatDto(BigDecimal.ZERO, seatNumber, occasionSeatLoaderDto.getOccasionDto(),
                                occasionSeatLoaderDto.isBooked(), occasionSeatLoaderDto.getSeatPlaceType()));
                    }
                });
                if (occasionLoaderDto.getNumberOfSeats() != occasionSeatDtoSet.size()) {
                    LOGGER.error("Number of seats is not equal to occasionSeatDto set size" + occasionLoaderDto.getOccasionName()
                            + " " + occasionLoaderDto.getOccasionAddress() + " "
                            + occasionLoaderDto.getOccasionTime() + " " + occasionLoaderDto.getTicketType());
                    return;
                }
                occasionLoaderDto.setOccasionSeatDto(occasionSeatDtoSet);
                OccasionEntity occasionEntity = occasionLoaderDtoToOccasionEntityTransformer.transform(occasionLoaderDto);
                occasionEntity.getOccasionSeatEntitySet().forEach(occasionSeatEntity -> occasionSeatEntity.setOccasionEntity(occasionEntity));
                occasionEntities.add(occasionEntity);
            });
            return Optional.of(occasionEntities);
        } catch (JsonMappingException e) {
            LOGGER.error("File has null values " + filePath + " " + e.getMessage());
            return Optional.empty();
        } catch (NullPointerException e) {
            LOGGER.error("Corrupted file " + filePath + " " + e.getMessage());
            return Optional.empty();
        } catch (IOException e) {
            LOGGER.error("Error while reading file " + filePath + " " + e.getMessage());
            return Optional.empty();
        }
    }
}
