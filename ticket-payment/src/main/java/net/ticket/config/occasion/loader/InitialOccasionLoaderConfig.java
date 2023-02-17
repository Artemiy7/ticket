package net.ticket.config.occasion.loader;

import lombok.Getter;
import lombok.Setter;
import net.ticket.enums.occasionloader.OccasionLoader;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "service.occasions")
public class InitialOccasionLoaderConfig {
    private List<String> occasionLoaderDtoList;
    private List<OccasionLoader> occasionLoaderFileList;
}
