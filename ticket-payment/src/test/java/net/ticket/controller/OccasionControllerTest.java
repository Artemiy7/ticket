package net.ticket.controller;

import lombok.NoArgsConstructor;
import net.ticket.constant.enums.ticket.SeatPlaceType;
import net.ticket.constant.enums.ticket.TicketType;
import net.ticket.controller.occasion.OccasionController;
import net.ticket.dto.occasion.OccasionDto;
import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.service.occasion.OccasionService;
import net.ticket.ticketexception.occasion.NoSuchOccasionException;
import net.ticket.util.ValidatorUtils;
import org.apache.catalina.connector.RequestFacade;
import org.bouncycastle.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnit4.class)
class OccasionControllerTest {
    @Mock
    private OccasionService occasionService;
    @Mock
    private ValidatorUtils validatorUtils;
    private OccasionDto occasionDto;
    private OccasionSeatDto occasionSeatDto;

    @BeforeEach
    public void init() {
        occasionSeatDto = OccasionSeatDto.builder()
                .seat((short)10)
                .cost(BigDecimal.TEN)
                .isBooked(false)
                .seatPlaceType(SeatPlaceType.CONCERT_STADION_SEAT)
                .build();

        occasionDto = OccasionDto.builder()
                .daysToOccasion((short) 10)
                .occasionTime(LocalDateTime.now())
                .numberOfSeats((short) 1)
                .occasionName("Concert Club - Test")
                .occasionAddress("Test address 10")
                .ticketType(TicketType.CONCERT_STADION)
                .daysToOccasion((short) 10)
                .occasionSeatDto(Set.of(occasionSeatDto)).build();
    }


    @Test
    public void findOccasionByIdOkTest() {
        OccasionController occasionController = new OccasionController(occasionService, validatorUtils);
        HttpServletRequest  httpServletRequest = mock(RequestFacade.class);

        when(occasionService.findOccasionWithOccasionSeats(any(Long.class))).thenReturn(Optional.of(occasionDto));
        when(httpServletRequest.getRequestURI()).thenReturn("/findOccasionById/100000001");

        ResponseEntity<OccasionDto> occasionDtoResponseEntity = occasionController.getOccasionById(100000001l, httpServletRequest);

        verify(occasionService, Mockito.times(1)).findOccasionWithOccasionSeats(100000001l);
        assert Objects.areEqual(occasionDtoResponseEntity.getBody(), occasionDto);
        assert occasionDtoResponseEntity.getHeaders().get("path").get(0).equals("/findOccasionById/100000001");
        assert occasionDtoResponseEntity.getHeaders().get("Content-Type").get(0).equals("application/json");
        assert occasionDtoResponseEntity.getStatusCode().equals(HttpStatus.OK);
    }

    @Test
    public void findOccasionByIdNoSuchOccasionTest() {
//        OccasionController occasionController = new OccasionController(occasionService, validatorUtils);
//        HttpServletRequest  httpServletRequest = mock(RequestFacade.class);
//
//        given(occasionService.findOccasionWithOccasionSeats(any(Long.class))).willThrow(new NoSuchOccasionException("No such OccasionEntity 100000001"));
//        given(httpServletRequest.getRequestURI()).willReturn("/findOccasionById/100000001");
//
//        when(occasionController.getOccasionById(100000001, httpServletRequest));//.thenReturn(ResponseEntity.of(null));

//        verify(occasionService, Mockito.times(1)).findOccasionWithOccasionSeats(100000001l);
//        assert Objects.areEqual(occasionDtoResponseEntity.getBody(), occasionDto);
//        assert occasionDtoResponseEntity.getHeaders().get("path").get(0).equals("/findOccasionById/100000001");
//        assert occasionDtoResponseEntity.getHeaders().get("Content-Type").get(0).equals("application/json");
//        assert occasionDtoResponseEntity.getStatusCode().equals(HttpStatus.OK);
    }
}
