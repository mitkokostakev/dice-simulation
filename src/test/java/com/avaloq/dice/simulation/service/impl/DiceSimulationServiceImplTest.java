package com.avaloq.dice.simulation.service.impl;

import com.avaloq.dice.simulation.domain.DiceSimulationResponse;
import com.avaloq.dice.simulation.service.DiceSimulationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = { DiceSimulationServiceImpl.class, ValidationAutoConfiguration.class})
class DiceSimulationServiceImplTest {

    @Autowired
    private DiceSimulationService diceSimulationService;

    @Test
    void testCreate() {

        // Given
        int numberOfDices = 3;
        int sidesOfDice = 6;
        int numberOfRolls = 100;

        // When
        DiceSimulationResponse diceSimulationResponse = diceSimulationService.create(numberOfDices, sidesOfDice, numberOfRolls);

        // Then
        assertNotNull(diceSimulationResponse);
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