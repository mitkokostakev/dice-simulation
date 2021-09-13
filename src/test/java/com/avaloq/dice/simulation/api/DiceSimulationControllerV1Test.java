package com.avaloq.dice.simulation.api;

import com.avaloq.dice.simulation.api.impl.DiceSimulationControllerV1;
import com.avaloq.dice.simulation.domain.dto.DiceSimulationRelativeDistributionsResponse;
import com.avaloq.dice.simulation.domain.dto.DiceSimulationResponse;
import com.avaloq.dice.simulation.domain.entity.DiceSimulation;
import com.avaloq.dice.simulation.domain.mapper.DiceSimulationMapper;
import com.avaloq.dice.simulation.exception.dto.ErrorsDto;
import com.avaloq.dice.simulation.service.DiceSimulationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DiceSimulationControllerV1.class)
class DiceSimulationControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockBean
    private DiceSimulationService diceSimulationService;

    @MockBean
    private DiceSimulationMapper mapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreate() throws Exception {
        // Given
        int numberOfDices = 3;
        int sidesOfDice = 6;
        int numberOfRolls = 100;

        DiceSimulation diceSimulation = DiceSimulation.builder().build();
        DiceSimulationResponse diceSimulationResponse = DiceSimulationResponse.builder().build();

        when(diceSimulationService.createAndSave(numberOfDices, sidesOfDice, numberOfRolls))
                .thenReturn(diceSimulation);
        when(mapper.mapFromEntity(any())).thenReturn(diceSimulationResponse);

        // When
        MvcResult mvcResult =
                this.mockMvc
                        .perform(
                                post("/api/v1/simulations")
                                        .param("numberOfDices", String.valueOf(numberOfDices))
                                        .param("sidesOfDice", String.valueOf(sidesOfDice))
                                        .param("numberOfRolls", String.valueOf(numberOfRolls))
                                        .characterEncoding(StandardCharsets.UTF_8.toString())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andReturn();
        String contentResponse = mvcResult.getResponse().getContentAsString();

        DiceSimulationResponse response = objectMapper.readValue(contentResponse, DiceSimulationResponse.class);

        // Then
        assertEquals(diceSimulationResponse, response);

        verify(diceSimulationService).createAndSave(numberOfDices, sidesOfDice, numberOfRolls);
        verify(mapper).mapFromEntity(any());
    }

    @Test
    void testCreate_InvalidNumberOfDices() throws Exception {
        // Given
        int numberOfDices = 0;
        int sidesOfDice = 6;
        int numberOfRolls = 100;

        // When
        MvcResult mvcResult =
                this.mockMvc
                        .perform(
                                post("/api/v1/simulations")
                                        .param("numberOfDices", String.valueOf(numberOfDices))
                                        .param("sidesOfDice", String.valueOf(sidesOfDice))
                                        .param("numberOfRolls", String.valueOf(numberOfRolls))
                                        .characterEncoding(StandardCharsets.UTF_8.toString())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andReturn();
        String contentResponse = mvcResult.getResponse().getContentAsString();

        ErrorsDto errorsDto = objectMapper.readValue(contentResponse, ErrorsDto.class);

        // Then
        assertEquals("must be greater than or equal to 1", errorsDto.getErrors().get(0).getMessage());

        verifyNoInteractions(diceSimulationService);
    }

    @Test
    void testCreate_InvalidSidesOfDice() throws Exception {
        // Given
        int numberOfDices = 2;
        int sidesOfDice = 3;
        int numberOfRolls = 100;

        // When
        MvcResult mvcResult =
                this.mockMvc
                        .perform(
                                post("/api/v1/simulations")
                                        .param("numberOfDices", String.valueOf(numberOfDices))
                                        .param("sidesOfDice", String.valueOf(sidesOfDice))
                                        .param("numberOfRolls", String.valueOf(numberOfRolls))
                                        .characterEncoding(StandardCharsets.UTF_8.toString())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andReturn();
        String contentResponse = mvcResult.getResponse().getContentAsString();

        ErrorsDto errorsDto = objectMapper.readValue(contentResponse, ErrorsDto.class);

        // Then
        assertEquals("must be greater than or equal to 4", errorsDto.getErrors().get(0).getMessage());

        verifyNoInteractions(diceSimulationService);
    }

    @Test
    void testCreate_InvalidNumberOfRolls() throws Exception {
        // Given
        int numberOfDices = 2;
        int sidesOfDice = 5;
        int numberOfRolls = 0;

        // When
        MvcResult mvcResult =
                this.mockMvc
                        .perform(
                                post("/api/v1/simulations")
                                        .param("numberOfDices", String.valueOf(numberOfDices))
                                        .param("sidesOfDice", String.valueOf(sidesOfDice))
                                        .param("numberOfRolls", String.valueOf(numberOfRolls))
                                        .characterEncoding(StandardCharsets.UTF_8.toString())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andReturn();
        String contentResponse = mvcResult.getResponse().getContentAsString();

        ErrorsDto errorsDto = objectMapper.readValue(contentResponse, ErrorsDto.class);

        // Then
        assertEquals("must be greater than or equal to 1", errorsDto.getErrors().get(0).getMessage());

        verifyNoInteractions(diceSimulationService);
    }

    @Test
    void testQueryRelativeDistributions_NumberOfDices3_Sides6() throws Exception {
        // Given
        int numberOfDices = 3;
        int sidesOfDice = 6;

        final FileInputStream responseFis = new FileInputStream(
                "src/test/resources/payloads/NumberOfDices3_SidesOfDice6_RelativeDistributionsResponse.json");
        final String contentResponseJSON = IOUtils.toString(responseFis, "UTF-8");
        final DiceSimulationRelativeDistributionsResponse expected = objectMapper.readValue(contentResponseJSON,
                DiceSimulationRelativeDistributionsResponse.class);

        when(diceSimulationService.queryRelativeDistributions(numberOfDices, sidesOfDice))
                .thenReturn(expected);

        // When
        MvcResult mvcResult =
                this.mockMvc
                        .perform(
                                get("/api/v1/simulations")
                                        .param("numberOfDices", String.valueOf(numberOfDices))
                                        .param("sidesOfDice", String.valueOf(sidesOfDice))
                                        .characterEncoding(StandardCharsets.UTF_8.toString())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();
        String contentResponse = mvcResult.getResponse().getContentAsString();

        DiceSimulationRelativeDistributionsResponse response = objectMapper.readValue(contentResponse,
                DiceSimulationRelativeDistributionsResponse.class);

        // Then
        assertEquals(expected, response);

        verify(diceSimulationService).queryRelativeDistributions(numberOfDices, sidesOfDice);
    }
}