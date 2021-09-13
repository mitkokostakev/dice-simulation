package com.avaloq.dice.simulation.service;

import com.avaloq.dice.simulation.domain.dto.DiceSimulationRelativeDistributionsResponse;
import com.avaloq.dice.simulation.domain.entity.DiceSimulation;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

@Validated
public interface DiceSimulationService {

    DiceSimulationRelativeDistributionsResponse queryRelativeDistributions(int numberOfDices, int sidesOfDice);

    DiceSimulation createAndSave(@Min(value = 1) int numberOfDices, @Min(value = 4) int sidesOfDice,
                                 @Min(value = 1) int numberOfRolls);

    DiceSimulation createDiceSimulation(int numberOfDices, int sidesOfDice, int numberOfRolls);
}
