package net.ticket.initialdataloader;

import net.ticket.initialdataloader.occasion.loaders.InitialOccasionDataLoaderSubFacade;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoaderFacade {
    public final InitialOccasionDataLoaderSubFacade initialOccasionDataLoaderSubFacade;

    public InitialDataLoaderFacade(InitialOccasionDataLoaderSubFacade initialOccasionDataLoaderSubFacade) {
        this.initialOccasionDataLoaderSubFacade = initialOccasionDataLoaderSubFacade;
    }

    public void loadInitialDataAndSave() {
        initialOccasionDataLoaderSubFacade.loadSubFacadeInitialDataAndSave();
    }
}
