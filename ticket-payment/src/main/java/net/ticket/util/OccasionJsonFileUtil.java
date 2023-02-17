package net.ticket.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.Sets;
import net.ticket.dto.occasion.occasionloader.OccasionLoaderDto;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@Component
public class OccasionJsonFileUtil {
    private final ObjectMapper initialLoaderObjectMapper;

    public OccasionJsonFileUtil(ObjectMapper initialLoaderObjectMapper) {
        this.initialLoaderObjectMapper = initialLoaderObjectMapper;
    }

    public Set<OccasionLoaderDto> loaderOccasionsFromJson(File file) throws IOException {
        return Sets.newHashSet(initialLoaderObjectMapper.readValue(file, OccasionLoaderDto[].class));
    }

    public void writeToFileAndFlush(File file, Set<OccasionLoaderDto> occasionLoaderDtotSet) throws FileNotFoundException, JsonProcessingException {
        try(PrintWriter out = new PrintWriter(file)) {
            ObjectWriter ow = initialLoaderObjectMapper.writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(occasionLoaderDtotSet);
            out.write(json);
            out.flush();
        }
    }
}
