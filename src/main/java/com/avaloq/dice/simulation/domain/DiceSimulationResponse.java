package com.avaloq.dice.simulation.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(
        value = "DiceSimulation",
        description = "Record of Dice Simulation")
public class DiceSimulationResponse {

    @Min(value = 1)
    private int numberOfDices;

    @Min(value = 4)
    private int sidesOfDice;

    @Min(value = 1)
    private int numberOfRolls;

    private Map<Integer, Long> relativeDistributions;

}
