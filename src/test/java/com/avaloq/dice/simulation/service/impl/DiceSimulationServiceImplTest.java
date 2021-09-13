package com.avaloq.dice.simulation.service.impl;

import com.avaloq.dice.simulation.domain.dto.DiceSimulationRelativeDistributionsResponse;
import com.avaloq.dice.simulation.domain.entity.DiceSimulation;
import com.avaloq.dice.simulation.domain.repository.DiceSimulationRepository;
import com.avaloq.dice.simulation.exception.ResourceNotFoundException;
import com.avaloq.dice.simulation.service.DiceSimulationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import javax.validation.ConstraintViolationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = { DiceSimulationServiceImpl.class, ValidationAutoConfiguration.class})
@MockBeans({ @MockBean(DiceSimulationRepository.class) })
class DiceSimulationServiceImplTest {

    @Autowired
    private DiceSimulationService diceSimulationService;

    @Autowired
    private DiceSimulationRepository repository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreate() {

        // Given
        int numberOfDices = 3;
        int sidesOfDice = 6;
        int numberOfRolls = 100;

        final DiceSimulation diceSimulation = DiceSimulation.builder()
                .numberOfDices(numberOfDices)
                .sidesOfDice(sidesOfDice)
                .numberOfRolls(numberOfRolls)
                .build();

        when(repository.save(any(DiceSimulation.class))).thenReturn(diceSimulation);

        // When
        DiceSimulation created = diceSimulationService.createAndSave(numberOfDices, sidesOfDice, numberOfRolls);

        // Then
        assertNotNull(created);

        verify(repository).save(any(DiceSimulation.class));
    }

    @Test
    void testCreate_invalidNumberOfDices() {

        // Given
        int numberOfDices = 0;
        int sidesOfDice = 4;
        int numberOfRolls = 100;

        assertThatThrownBy(() -> diceSimulationService.createAndSave(numberOfDices, sidesOfDice, numberOfRolls))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must be greater than or equal to 1");
    }

    @Test
    void testCreate_invalidSidesOfDice() {

        // Given
        int numberOfDices = 2;
        int sidesOfDice = 3;
        int numberOfRolls = 100;

        assertThatThrownBy(() -> diceSimulationService.createAndSave(numberOfDices, sidesOfDice, numberOfRolls))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must be greater than or equal to 4");
    }

    @Test
    void testCreate_invalidNumberOfRolls() {

        // Given
        int numberOfDices = 2;
        int sidesOfDice = 3;
        int numberOfRolls = 0;

        assertThatThrownBy(() -> diceSimulationService.createAndSave(numberOfDices, sidesOfDice, numberOfRolls))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must be greater than or equal to 1");
    }

    @Test
    void testQueryRelativeDistributions() throws IOException {

        // Given
        int numberOfDices = 3;
        int sidesOfDice = 6;
        int numberOfRolls = 100;

        final List<DiceSimulation> simulations = new ArrayList<>();
        final Map<Integer, Long> relativeDistributions = new HashMap<>(
                Map.of(4,20L, 5,34L, 6,42L, 7,4L));

        final FileInputStream responseFis = new FileInputStream(
                "src/test/resources/payloads/RelativeDistributionsResponse.json");
        final String contentResponse = IOUtils.toString(responseFis, "UTF-8");
        final DiceSimulationRelativeDistributionsResponse expected = objectMapper.readValue(contentResponse,
                                                                    DiceSimulationRelativeDistributionsResponse.class);

        final DiceSimulation diceSimulation = DiceSimulation.builder()
                .numberOfDices(numberOfDices)
                .sidesOfDice(sidesOfDice)
                .numberOfRolls(numberOfRolls)
                .relativeDistributions(relativeDistributions)
                .build();
        simulations.add(diceSimulation);

        final DiceSimulation diceSimulation2 = DiceSimulation.builder()
                .numberOfDices(numberOfDices)
                .sidesOfDice(sidesOfDice)
                .numberOfRolls(numberOfRolls)
                .build();
        simulations.add(diceSimulation2);

        when(repository.findByNumberOfDicesAndSidesOfDice(numberOfDices, sidesOfDice)).thenReturn(Optional.of(simulations));

        // When
        DiceSimulationRelativeDistributionsResponse response = diceSimulationService.queryRelativeDistributions(numberOfDices, sidesOfDice);

        // Then
        assertNotNull(response);
        assertEquals(expected, response);

        verify(repository).findByNumberOfDicesAndSidesOfDice(numberOfDices, sidesOfDice);
    }

    @Test
    void testQueryRelativeDistributions_NumberOfDice2_Sides4() throws IOException {

        // Given
        int numberOfDices = 2;
        int sidesOfDice = 4;

        final FileInputStream simulationsFis = new FileInputStream(
                "src/test/resources/payloads/Simulations_numberOfDices2_sidesOfDice4.json");
        final String simulationsJson = IOUtils.toString(simulationsFis, "UTF-8");
        final List<DiceSimulation> simulations = objectMapper.readValue(simulationsJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, DiceSimulation.class));

        final FileInputStream responseFis = new FileInputStream(
                "src/test/resources/payloads/NumberOfDices2_SidesOfDice4_RelativeDistributionsResponse.json");
        final String contentResponse = IOUtils.toString(responseFis, "UTF-8");
        final DiceSimulationRelativeDistributionsResponse expected = objectMapper.readValue(contentResponse,
                DiceSimulationRelativeDistributionsResponse.class);

        when(repository.findByNumberOfDicesAndSidesOfDice(numberOfDices, sidesOfDice)).thenReturn(Optional.of(simulations));

        // When
        DiceSimulationRelativeDistributionsResponse response = diceSimulationService.queryRelativeDistributions(numberOfDices, sidesOfDice);

        // Then
        assertNotNull(response);
        assertEquals(expected, response);

        verify(repository).findByNumberOfDicesAndSidesOfDice(numberOfDices, sidesOfDice);
    }

    @Test
    void testQueryRelativeDistributions_NumberOfDice3_Sides6() throws IOException {

        // Given
        int numberOfDices = 3;
        int sidesOfDice = 6;

        final FileInputStream simulationsFis = new FileInputStream(
                "src/test/resources/payloads/Simulations_numberOfDices3_sidesOfDice6.json");
        final String simulationsJson = IOUtils.toString(simulationsFis, "UTF-8");
        final List<DiceSimulation> simulations = objectMapper.readValue(simulationsJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, DiceSimulation.class));

        final FileInputStream responseFis = new FileInputStream(
                "src/test/resources/payloads/NumberOfDices3_SidesOfDice6_RelativeDistributionsResponse.json");
        final String contentResponse = IOUtils.toString(responseFis, "UTF-8");
        final DiceSimulationRelativeDistributionsResponse expected = objectMapper.readValue(contentResponse,
                DiceSimulationRelativeDistributionsResponse.class);

        when(repository.findByNumberOfDicesAndSidesOfDice(numberOfDices, sidesOfDice)).thenReturn(Optional.of(simulations));

        // When
        DiceSimulationRelativeDistributionsResponse response = diceSimulationService.queryRelativeDistributions(numberOfDices, sidesOfDice);

        // Then
        assertNotNull(response);
        assertEquals(expected, response);

        verify(repository).findByNumberOfDicesAndSidesOfDice(numberOfDices, sidesOfDice);
    }

    @Test
    void testQueryRelativeDistributions_ResourceNotFound() {

        // Given
        int numberOfDices = 3;
        int sidesOfDice = 6;

        when(repository.findByNumberOfDicesAndSidesOfDice(numberOfDices, sidesOfDice)).thenThrow(new ResourceNotFoundException());

        // When
        assertThatThrownBy(() -> diceSimulationService.queryRelativeDistributions(numberOfDices, sidesOfDice))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Resource does not exist...");

        verify(repository).findByNumberOfDicesAndSidesOfDice(numberOfDices, sidesOfDice);
    }

}