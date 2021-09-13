package com.avaloq.dice.simulation.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "RelativeDistributions", description = "Relative Distributions Statistics of Rolling Dice Simulations")
public class DiceSimulationRelativeDistributionsResponse {
    private int totalRolls;
    private int totalSimulations;
    private Map<Integer, BigDecimal> relativeDistributions;
}
