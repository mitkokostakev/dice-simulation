package com.avaloq.dice.simulation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiceSimulationRelativeDistributionsSummarized {
    private int totalRolls;
    private int totalSimulations;
    private Map<Integer, Long> relativeDistributions;
}
