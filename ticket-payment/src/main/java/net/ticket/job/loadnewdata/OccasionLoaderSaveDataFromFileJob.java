package net.ticket.job.loadnewdata;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import net.ticket.initialdataloader.InitialDataLoaderFacade;
import net.ticket.repository.job.ScheduledJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OccasionLoaderSaveDataFromFileJob {
    private final static Logger LOGGER = LoggerFactory.getLogger(OccasionLoaderSaveDataFromFileJob.class);
    private static int count = 0;
    private final String instanceId;
    private final ScheduledJobRepository scheduledJobRepository;
    private final InitialDataLoaderFacade initialDataLoaderFacade;

    public OccasionLoaderSaveDataFromFileJob(@Value("${eureka.instance.metadataMap.instanceId}")String instanceId,
                                             ScheduledJobRepository scheduledJobRepository,
                                             InitialDataLoaderFacade initialDataLoaderFacade) {
        this.instanceId = instanceId;
        this.scheduledJobRepository = scheduledJobRepository;
        this.initialDataLoaderFacade = initialDataLoaderFacade;
    }

    @Scheduled(cron = "${service.jobs.occasionLoader.occasionLoaderSaveDataFromFileJob.execution-time}")
    @SchedulerLock(name = "OccasionLoaderSaveDataFromFileJob")
    public void job() {
        try {
            initialDataLoaderFacade.loadInitialDataAndSave();
            scheduledJobRepository.insertSchedulerLockLog(instanceId + ", call count: " + count + ", job: " + "OccasionLoaderSaveInitialDataJob");
            LOGGER.info(instanceId + ", call count: " + ++count + " OccasionLoaderSaveInitialDataJob");
        } catch (Exception e) {
            LOGGER.error(instanceId + ", call count: " + ++count + ", " + "failed with: " + e.getMessage());
        }
    }
}
