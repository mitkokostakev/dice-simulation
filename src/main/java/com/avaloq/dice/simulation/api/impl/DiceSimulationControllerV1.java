package com.avaloq.dice.simulation.api.impl;

import com.avaloq.dice.simulation.api.DiceSimulationController;
import com.avaloq.dice.simulation.domain.DiceSimulationResponse;
import com.avaloq.dice.simulation.service.DiceSimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@Validated
@RestController
@RequiredArgsConstructor
public class DiceSimulationControllerV1 implements DiceSimulationController {

    private final DiceSimulationService service;

    @Override
    public DiceSimulationResponse create(@RequestParam @Min(value = 1) int numberOfDices,
                                         @RequestParam @Min(value = 4) int sidesOfDice,
                                         @RequestParam @Min(value = 1) int numberOfRolls) {
        return service.create(numberOfDices, sidesOfDice, numberOfRolls);
    }

}
