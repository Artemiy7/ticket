package net.ticket.controller.occasion;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.ticket.domain.pagination.PageAndSortingObject;
import net.ticket.dto.occasion.OccasionDto;
import net.ticket.constant.enums.search.occasion.OccasionQueryParameterOperation;
import net.ticket.response.error.ErrorResponse;
import net.ticket.service.occasion.OccasionService;
import net.ticket.ticketexception.occasion.NoSuchOccasionException;
import net.ticket.util.ValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Api(value = "Performs occasion operations")
@RestController
@RequestMapping("api/v1/occasions")
public class OccasionController {
    private final static Logger LOGGER = LoggerFactory.getLogger(OccasionController.class);
    private final OccasionService occasionService;
    private final ValidatorUtils validatorUtils;

    public OccasionController(OccasionService occasionService,
                              ValidatorUtils validatorUtils) {
        this.occasionService = occasionService;
        this.validatorUtils = validatorUtils;
    }

    @ApiOperation(value = "Selects OccasionDto by id with OccasionSeatsDto and calculates OccasionSeatDto cost by date, seatType and number of booked seats", response = OccasionDto.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OccasionDto> getOccasionById(@PathVariable long id) {
        OccasionDto occasionDto = occasionService.findOccasionWithOccasionSeats(id).orElseThrow(() -> new NoSuchOccasionException("No such OccasionEntity " + id));
        LOGGER.info("OccasionEntity found " + id);
        validatorUtils.validationBeforeDeserialization(occasionDto);
        occasionDto.getOccasionSeatDto().forEach(validatorUtils::validationBeforeDeserialization);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(occasionDto);
    }

    @ApiOperation(value = "Select and filter OccasionDto without OccasionSeatDto", response = List.class)
    @GetMapping
    public ResponseEntity<List<OccasionDto>> findOccasions(@RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "occasionTime", name = "sorting_field") String sortingField,
                                                           @RequestParam(defaultValue = "ASC", name = "sorting_order") String sortingOrder,
                                                           @RequestParam MultiValueMap<String, String> searchMap,
                                                           final HttpServletRequest httpServletRequest) {

        String requestPath = httpServletRequest.getRequestURI() + "/" + httpServletRequest.getQueryString();
        try {
            PageAndSortingObject pageAndSortingObject = PageAndSortingObject.createObject(size, page, sortingField, PageAndSortingObject.SortingOrder.valueOf(sortingOrder));
            Optional<List<OccasionDto>> occasionDtoList = occasionService.findOccasionsByParameters(searchMap, pageAndSortingObject);
            if (occasionDtoList.isEmpty()) {
                return new ResponseEntity(ErrorResponse.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message("No OccasionEntity was found")
                        .localDateTime(LocalDateTime.now())
                        .path(requestPath)
                        .build(), HttpStatus.NOT_FOUND);
            }
            LOGGER.info("Occasions found " + occasionDtoList.get().size());
            return ResponseEntity.ok()
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .body(occasionDtoList.get());

        } catch (IllegalArgumentException e) {
            HttpHeaders headers = new HttpHeaders();
            LOGGER.error("Wrong filter type or value " + e.getMessage());
            headers.add("Error-Message", "Wrong query type or value " + e.getMessage());
            return new ResponseEntity(ErrorResponse.builder()
                                                   .httpStatus(HttpStatus.BAD_REQUEST)
                                                   .message(e.getMessage())
                                                   .localDateTime(LocalDateTime.now())
                                                   .path(requestPath)
                                                   .build(), headers, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Returns number of filter parameters")
    @RequestMapping(value = "/fetchFilters", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OccasionQueryParameterOperation>> fetchOccasionFilters() {
        return ResponseEntity.ok()
                             .body(OccasionQueryParameterOperation.getOccasionFilterTypeList());
    }
}
