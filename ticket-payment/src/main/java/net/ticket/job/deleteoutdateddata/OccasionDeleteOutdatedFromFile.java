package net.ticket.job.deleteoutdateddata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.collect.Sets;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import net.ticket.dto.occasion.occasionloader.OccasionLoaderDto;
import net.ticket.enums.occasionloader.OccasionLoader;
import net.ticket.repository.job.ScheduledJobRepository;
import net.ticket.service.occasion.OccasionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OccasionDeleteOutdatedFromFile {
    private final static Logger LOGGER = LoggerFactory.getLogger(OccasionDeleteOutdatedFromFile.class);
    private final ObjectMapper initialLoaderObjectMapper;
    private final String instanceId;
    private final String filePath = OccasionLoader.CONCERT_CLUB_FILE.getFilePath();
    private static int count = 0;
    private final ScheduledJobRepository scheduledJobRepository;
    private final OccasionService occasionService;

    public OccasionDeleteOutdatedFromFile(@Value("${eureka.instance.metadataMap.instanceId}") String instanceId,
                                          ObjectMapper initialLoaderObjectMapper,
                                         ScheduledJobRepository scheduledJobRepository,
                                         OccasionService occasionService) {
        this.instanceId = instanceId;
        this.initialLoaderObjectMapper = initialLoaderObjectMapper;
        this.scheduledJobRepository = scheduledJobRepository;
        this.occasionService = occasionService;
    }

    @Scheduled(cron = "*/7 * * * * *")
    @SchedulerLock(name = "OccasionDeleteOutdatedFromFile")
    public void job() {
        try {
            File file = new File(filePath+"DDDDDDDD");
            Set<OccasionLoaderDto> occasionLoaderDtotSet = Sets.newHashSet(initialLoaderObjectMapper.readValue(file, OccasionLoaderDto[].class));
            //List<OccasionLoaderDto> occasionLoaderDtoList = Arrays.asList();
            occasionLoaderDtotSet = occasionLoaderDtotSet.stream()
                                                         .filter(occasionLoaderDto -> occasionLoaderDto.getOccasionTime().isAfter(LocalDateTime.now().minusMinutes(30)))
                                                         .collect(Collectors.toSet());

            PrintWriter out = new PrintWriter(file);
            ObjectWriter ow = initialLoaderObjectMapper.writer().withDefaultPrettyPrinter();


            String json = ow.writeValueAsString(occasionLoaderDtotSet);

            out.write(json);
            out.flush();
            scheduledJobRepository.insertSchedulerLockLog(instanceId + ", call count: " + count + ", job: " + LOGGER.getName());
            LOGGER.info(instanceId + ", call count: " + ++count);
        } catch (Exception e) {
            LOGGER.error(instanceId + ", call count: " + ++count + ", " + "failed with: " + e.getMessage());
        }
    }
}
