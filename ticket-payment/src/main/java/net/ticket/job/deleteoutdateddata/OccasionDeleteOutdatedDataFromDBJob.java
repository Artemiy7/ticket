package net.ticket.job.deleteoutdateddata;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import net.ticket.repository.job.ScheduledJobRepository;
import net.ticket.service.occasion.OccasionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OccasionDeleteOutdatedDataFromDBJob {
    private final static Logger LOGGER = LoggerFactory.getLogger(OccasionDeleteOutdatedDataFromDBJob.class);
    private static int count = 0;
    private final String instanceId;
    private final ScheduledJobRepository scheduledJobRepository;
    private final OccasionService occasionService;

    public OccasionDeleteOutdatedDataFromDBJob(@Value("${eureka.instance.metadataMap.instanceId}") String instanceId,
                                               ScheduledJobRepository scheduledJobRepository,
                                               OccasionService occasionService) {
        this.instanceId = instanceId;
        this.scheduledJobRepository = scheduledJobRepository;
        this.occasionService = occasionService;
    }

    @Scheduled(cron = "${service.jobs.occasionLoader.occasionDeleteOutdatedDataFromDBJob.execution-time}")
    @SchedulerLock(name = "OccasionDeleteOutdatedDataFromDBJob")
    public void job() {
        try {
            occasionService.setOutdatedOccasionNotActiveByCurrentDate();
            scheduledJobRepository.insertSchedulerLockLog(instanceId + ", call count: " + count + ", job: " + LOGGER.getName());
            LOGGER.info(instanceId + ", call count: " + ++count);
        } catch (Exception e) {
            LOGGER.error(instanceId + ", call count: " + ++count + ", " + "failed with: " + e.getMessage());
        }
    }
}
