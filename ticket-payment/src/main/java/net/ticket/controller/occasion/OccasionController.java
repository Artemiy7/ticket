package net.ticket.controller.occasion;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.ticket.dto.occasion.OccasionDto;
import net.ticket.request.pagination.PaginationRequest;
import net.ticket.constant.enums.filtertype.OccasionFilterType;
import net.ticket.response.error.ErrorResponse;
import net.ticket.service.occasion.OccasionService;
import net.ticket.util.ValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Api(value = "Performs occasion operations(select, filter)")
@RestController
@RequestMapping("/occasion")
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
    @RequestMapping(value = "/getOccasionById/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OccasionDto> getOccasionById(@PathVariable long id) {
        HttpHeaders headers = new HttpHeaders();
        Optional<OccasionDto> occasionDtoOptional = occasionService.findOccasionWithOccasionSeats(id);
        LOGGER.info("OccasionEntity found " + id);
        occasionDtoOptional.get().getOccasionSeatDto().forEach(occasionSeatDto -> occasionSeatDto.setCost(BigDecimal.ZERO));
        validatorUtils.validationBeforeDeserialization(occasionDtoOptional.get());
        occasionDtoOptional.get().getOccasionSeatDto().forEach(validatorUtils::validationBeforeDeserialization);
        return ResponseEntity.ok()
                             .headers(headers)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(occasionDtoOptional.get());
    }

    @ApiOperation(value = "Select and filter OccasionDto without OccasionSeatDto", response = List.class)
    @RequestMapping(value = "/filterOccasion", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OccasionDto>> filterOccasions(@RequestParam MultiValueMap<String, String> multiValueMap,
                                                             @RequestBody PaginationRequest paginationRequest) {
        HttpHeaders headers = new HttpHeaders();

        if (multiValueMap.isEmpty()) {
            LOGGER.error("No such OccasionS in dataBase");
            headers.add("OccasionFilter-message", "Filter is absent");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .headers(headers)
                                 .build();
        }
        try {
            Optional<List<OccasionDto>> occasionDtoList = occasionService.findFilteredOccasions(multiValueMap, paginationRequest);
            LOGGER.info("Occasions found ".concat(String.valueOf(occasionDtoList.get().size())));
            occasionDtoList.get().forEach(validatorUtils::validationBeforeDeserialization);
            return ResponseEntity.ok()
                                 .headers(headers)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .body(occasionDtoList.get());

        } catch (IllegalArgumentException e) {
            LOGGER.error("Wrong filter type or value " + e.getMessage());
            headers.add("Occasion-Filter-Error-Message", "Wrong filter type or value " + e.getMessage());
            return new ResponseEntity(ErrorResponse.builder()
                                                   .httpStatus(HttpStatus.BAD_REQUEST)
                                                   .message(e.getMessage())
                                                   .localDateTime(LocalDateTime.now())
                                                   .path("/occasion/filterOccasion")
                                                   .build(), headers, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Returns number of filter parameters", response = HttpStatus.class)
    @RequestMapping(value = "/fetchOccasionFilters", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OccasionFilterType>> fetchOccasionFilters() {
        return ResponseEntity.ok()
                             .body(OccasionFilterType.getOccasionFilterTypeList());
    }


}
