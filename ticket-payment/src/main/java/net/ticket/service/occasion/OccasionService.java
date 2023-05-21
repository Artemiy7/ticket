package net.ticket.service.occasion;

import net.ticket.dto.occasion.OccasionDto;
import net.ticket.entity.occasion.OccasionEntity;
import net.ticket.request.pagination.PaginationRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OccasionService {
    Optional<OccasionDto> findOccasionWithOccasionSeats(long id);
    Optional<List<OccasionDto>> findFilteredOccasions(Map<String, List<String>> filterMap, PaginationRequest paginationRequest);
    void saveOccasionList(List<OccasionEntity> occasionEntityList);
    void setOutdatedOccasionNotActiveByCurrentDate();
}
