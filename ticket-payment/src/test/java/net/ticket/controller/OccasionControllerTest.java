package net.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ticket.constant.enums.ticket.SeatPlaceType;
import net.ticket.constant.enums.ticket.TicketType;
import net.ticket.controller.occasion.OccasionController;
import net.ticket.dto.occasion.OccasionDto;
import net.ticket.dto.occasion.OccasionSeatDto;
import net.ticket.service.occasion.OccasionService;
import net.ticket.util.ValidatorUtils;
import org.bouncycastle.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnit4.class)
@AutoConfigureMockMvc
@SpringBootTest
class OccasionControllerTest {
    @Autowired
    OccasionController occasionController;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OccasionService occasionService;
    @MockBean
    private ValidatorUtils validatorUtils;
    @Autowired
    private ObjectMapper objectMapper;
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
    public void findOccasionByIdTest200() throws Exception {
        when(occasionService.findOccasionWithOccasionSeats(any(Long.class))).thenReturn(Optional.of(occasionDto));

        String response = mockMvc.perform(get("/occasion/findOccasionById/{id}", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(occasionDto)))
                .andExpect(status().isOk())
                .andExpect(header().string("path", "/occasion/findOccasionById/1"))
                .andReturn().getResponse().getContentAsString();

        assert Objects.areEqual(response, objectMapper.writeValueAsString(occasionDto));
    }

    @Test
    public void findOccasionByIdTest404() throws Exception {
        when(occasionService.findOccasionWithOccasionSeats(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/occasion/findOccasionById/{id}", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(occasionDto)))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Occasion-message", "No such OccasionEntity 1"));
    }
}
