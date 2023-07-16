package net.ticket.service.occasion;

import net.ticket.domain.pagination.PageAndSortingObject;
import net.ticket.dto.occasion.OccasionDto;
import net.ticket.domain.entity.occasion.OccasionEntity;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

public interface OccasionService {
    Optional<OccasionDto> findOccasionWithOccasionSeats(long id);
    Optional<List<OccasionDto>> findOccasionsByParameters(MultiValueMap<String, String> searchMap, PageAndSortingObject pageAndSortingObject);
    void saveOccasionList(List<OccasionEntity> occasionEntityList);
    void setOutdatedOccasionNotActiveByCurrentDate();
}
