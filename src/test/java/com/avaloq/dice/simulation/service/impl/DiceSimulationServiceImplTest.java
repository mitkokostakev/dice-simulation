package com.avaloq.dice.simulation.service.impl;

import com.avaloq.dice.simulation.domain.entity.DiceSimulation;
import com.avaloq.dice.simulation.domain.repository.DiceSimulationRepository;
import com.avaloq.dice.simulation.service.DiceSimulationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.validation.ConstraintViolationException;

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

    @Test
    void testCreate() {

        // Given
        int numberOfDices = 3;
        int sidesOfDice = 6;
        int numberOfRolls = 100;

        DiceSimulation diceSimulation = DiceSimulation.builder()
                .numberOfDices(numberOfDices)
                .sidesOfDice(sidesOfDice)
                .numberOfRolls(numberOfRolls)
                .build();

        when(repository.save(any(DiceSimulation.class))).thenReturn(diceSimulation);

        // When
        DiceSimulation created = diceSimulationService.create(numberOfDices, sidesOfDice, numberOfRolls);

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

        assertThatThrownBy(() -> diceSimulationService.create(numberOfDices, sidesOfDice, numberOfRolls))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must be greater than or equal to 1");
    }

    @Test
    void testCreate_invalidSidesOfDice() {

        // Given
        int numberOfDices = 2;
        int sidesOfDice = 3;
        int numberOfRolls = 100;

        assertThatThrownBy(() -> diceSimulationService.create(numberOfDices, sidesOfDice, numberOfRolls))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must be greater than or equal to 4");
    }

    @Test
    void testCreate_invalidNumberOfRolls() {

        // Given
        int numberOfDices = 2;
        int sidesOfDice = 3;
        int numberOfRolls = 0;

        assertThatThrownBy(() -> diceSimulationService.create(numberOfDices, sidesOfDice, numberOfRolls))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("must be greater than or equal to 1");
    }

}