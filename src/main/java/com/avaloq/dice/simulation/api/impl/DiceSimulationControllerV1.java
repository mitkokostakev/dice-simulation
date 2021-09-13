package com.avaloq.dice.simulation.api.impl;

import com.avaloq.dice.simulation.api.DiceSimulationController;
import com.avaloq.dice.simulation.domain.dto.DiceSimulationRelativeDistributionsResponse;
import com.avaloq.dice.simulation.domain.dto.DiceSimulationResponse;
import com.avaloq.dice.simulation.domain.mapper.DiceSimulationMapper;
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
    private final DiceSimulationMapper mapper;

    @Override
    public DiceSimulationRelativeDistributionsResponse queryRelativeDistributions(int numberOfDices, int sidesOfDice) {
        return service.queryRelativeDistributions(numberOfDices, sidesOfDice);
    }

    @Override
    public DiceSimulationResponse create(@RequestParam @Min(value = 1) int numberOfDices,
                                         @RequestParam @Min(value = 4) int sidesOfDice,
                                         @RequestParam @Min(value = 1) int numberOfRolls) {
        return mapper.mapFromEntity(service.createAndSave(numberOfDices, sidesOfDice, numberOfRolls));
    }

}
