package net.ticket.initialdataloader.occasion.loaders;

import net.ticket.ApplicationContextProvider;
import net.ticket.config.occasion.loader.InitialOccasionLoaderConfig;
import net.ticket.domain.entity.occasion.OccasionEntity;
import net.ticket.initialdataloader.InitialLoader;
import net.ticket.service.occasion.OccasionService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InitialOccasionDataLoaderSubFacade {
    InitialOccasionLoaderConfig initialOccasionLoaderConfig;
    OccasionService occasionService;

    public InitialOccasionDataLoaderSubFacade(InitialOccasionLoaderConfig initialOccasionLoaderConfig,
                                              OccasionService occasionService) {
        this.initialOccasionLoaderConfig = initialOccasionLoaderConfig;
        this.occasionService = occasionService;
    }

    public void  loadSubFacadeInitialDataAndSave() {
        Set<InitialLoader> initialLoaderBeanList = new HashSet<>();
        initialOccasionLoaderConfig.getOccasionLoaderDtoList()
                .forEach(beanName -> {
                    initialLoaderBeanList.add(ApplicationContextProvider.getApplicationContext()
                                                                        .getBean(beanName, InitialLoader.class));
                });
        List<List<OccasionEntity>> loadedDataList = (List<List<OccasionEntity>>) initialLoaderBeanList.stream()
                .map(InitialLoader::loadInitialDataFromJson)
                .flatMap(optional -> optional.stream())
                .collect(Collectors.toList());

        List<OccasionEntity> occasionEntityList = loadedDataList.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        occasionService.saveOccasionList(occasionEntityList);
    }
}
