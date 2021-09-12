package com.avaloq.dice.simulation.service;

import com.avaloq.dice.simulation.domain.DiceSimulationResponse;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

@Validated
public interface DiceSimulationService {

    DiceSimulationResponse create(@Min(value = 1) int numberOfDices, @Min(value = 4) int sidesOfDice,
                                  @Min(value = 1) int numberOfRolls);

}
