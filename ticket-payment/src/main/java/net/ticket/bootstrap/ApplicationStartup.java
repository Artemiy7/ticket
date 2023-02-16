package net.ticket.bootstrap;

import net.ticket.config.occasion.loader.InitialOccasionLoaderConfig;
import net.ticket.initialdataloader.InitialDataLoaderFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@EnableConfigurationProperties(value = InitialOccasionLoaderConfig.class)
@Component
public class ApplicationStartup implements CommandLineRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationStartup.class);
    private final InitialDataLoaderFacade initialDataLoaderFacade;

    public ApplicationStartup(InitialDataLoaderFacade initialDataLoaderFacade) {
        this.initialDataLoaderFacade = initialDataLoaderFacade;
    }

    @Override
    public void run(String... args) {
        try {
            initialDataLoaderFacade.loadInitialDataAndSave();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}