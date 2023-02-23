package net.ticket.job.deleteoutdateddata;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import net.ticket.dto.occasion.occasionloader.OccasionLoaderDto;
import net.ticket.constant.enums.occasionloader.OccasionLoader;
import net.ticket.repository.job.ScheduledJobRepository;
import net.ticket.service.occasion.OccasionService;
import net.ticket.util.OccasionJsonFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OccasionDeleteOutdatedFromFile {
    private final static Logger LOGGER = LoggerFactory.getLogger(OccasionDeleteOutdatedFromFile.class);
    private static int count = 0;
    private final String filePath = OccasionLoader.CONCERT_CLUB_FILE.getFilePath();
    private final String instanceId;
    private final OccasionJsonFileUtil occasionJsonFileUtil;
    private final ScheduledJobRepository scheduledJobRepository;
    private final OccasionService occasionService;

    public OccasionDeleteOutdatedFromFile(@Value("${eureka.instance.metadataMap.instanceId}") String instanceId,
                                          OccasionJsonFileUtil occasionJsonFileUtil,
                                          ScheduledJobRepository scheduledJobRepository,
                                          OccasionService occasionService) {
        this.instanceId = instanceId;
        this.occasionJsonFileUtil = occasionJsonFileUtil;
        this.scheduledJobRepository = scheduledJobRepository;
        this.occasionService = occasionService;
    }

    @Scheduled(cron = "*/7 * * * * *")
    @SchedulerLock(name = "${service.jobs.occasionLoader.occasionDeleteOutdatedFromFile.execution-time}")
    public void job() {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Set<OccasionLoaderDto> occasionLoaderDtotSet = occasionJsonFileUtil.loaderOccasionsFromJson(file);
                occasionLoaderDtotSet = occasionLoaderDtotSet.stream()
                        .filter(occasionLoaderDto -> occasionLoaderDto.getOccasionTime().isAfter(LocalDateTime.now().minusMinutes(30)))
                        .collect(Collectors.toSet());

                occasionJsonFileUtil.writeToFileAndFlush(file, occasionLoaderDtotSet);
                scheduledJobRepository.insertSchedulerLockLog(instanceId + ", call count: " + count + ", job: " + LOGGER.getName());
                LOGGER.info(instanceId + ", call count: " + ++count);
            } else {
                LOGGER.error(instanceId + ", call count: " + ++count + ", " + " File is not exist " + filePath);
            }
        } catch (FileNotFoundException | JsonProcessingException e) {
            LOGGER.error(instanceId + ", call count: " + ++count + ", " + "failed with: " + e.getMessage());
        } catch (IOException e) {
            LOGGER.error(instanceId + ", call count: " + ++count + ", " + "failed with: " + e.getMessage());
        }
    }
}
