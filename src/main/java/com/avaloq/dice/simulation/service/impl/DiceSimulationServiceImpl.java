package com.avaloq.dice.simulation.service.impl;

import com.avaloq.dice.simulation.domain.dto.DiceSimulationRelativeDistributionsSummarized;
import com.avaloq.dice.simulation.domain.dto.DiceSimulationRelativeDistributionsResponse;
import com.avaloq.dice.simulation.domain.entity.DiceSimulation;
import com.avaloq.dice.simulation.domain.repository.DiceSimulationRepository;
import com.avaloq.dice.simulation.exception.ResourceNotFoundException;
import com.avaloq.dice.simulation.service.DiceSimulationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiceSimulationServiceImpl implements DiceSimulationService {

    private static final Random RANDOM = new Random();

    private final DiceSimulationRepository repository;

    @Override
    public DiceSimulationRelativeDistributionsResponse queryRelativeDistributions(int numberOfDices, int sidesOfDice) {
        final List<DiceSimulation> simulations = repository.findByNumberOfDicesAndSidesOfDice(numberOfDices, sidesOfDice)
                .orElseThrow(ResourceNotFoundException::new);
        return calculateRelativeDistributions(simulations);
    }

    private DiceSimulationRelativeDistributionsResponse calculateRelativeDistributions(List<DiceSimulation> simulations) {
        final DiceSimulationRelativeDistributionsSummarized summarized = DiceSimulationRelativeDistributionsSummarized
                .builder()
                .relativeDistributions(new HashMap<>())
                .build();
        int totalRolls = 0;
        int totalSimulations = simulations.size();

        for(DiceSimulation simulation : simulations) {
            totalRolls += simulation.getNumberOfRolls();
            Map<Integer, Long> relativeDistributions = simulation.getRelativeDistributions();
            if(ObjectUtils.isEmpty(relativeDistributions)) {
                continue;
            }
            final Map<Integer, Long> summarizedRelativeDistributions = summarized.getRelativeDistributions();
            for (Map.Entry<Integer, Long> entry : relativeDistributions.entrySet()) {
                log.debug(entry.getKey() + ":" + entry.getValue());
                Long value = summarizedRelativeDistributions.get(entry.getKey()) == null ? 0L :
                        summarizedRelativeDistributions.get(entry.getKey());
                value += entry.getValue();
                summarizedRelativeDistributions.put(entry.getKey(), value);
            }
        }
        summarized.setTotalSimulations(totalSimulations);
        summarized.setTotalRolls(totalRolls);
        return doCalculationForSummarizedData(summarized);
    }

    private DiceSimulationRelativeDistributionsResponse doCalculationForSummarizedData(DiceSimulationRelativeDistributionsSummarized summarized) {
        final DiceSimulationRelativeDistributionsResponse response = DiceSimulationRelativeDistributionsResponse.builder()
                .totalSimulations(summarized.getTotalSimulations())
                .totalRolls(summarized.getTotalRolls())
                .relativeDistributions(new HashMap<>())
                .build();

        // calculate relative distributions from summarized data collected
        final Map<Integer, Long> relativeDistributionsSummarized = summarized.getRelativeDistributions();

        if(!ObjectUtils.isEmpty(relativeDistributionsSummarized)) {
            for(Map.Entry<Integer, Long> entry : relativeDistributionsSummarized.entrySet() ) {
                // Calculate relative distribution
                BigDecimal value = new BigDecimal(entry.getValue())
                        .divide(new BigDecimal(summarized.getTotalRolls()), 8, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100))
                        .setScale(2, RoundingMode.HALF_UP);

                log.debug("Relative Distribution for {} calculated to : {}", entry.getKey(), value.doubleValue());
                response.getRelativeDistributions().put(entry.getKey(), value);
            }
        }
        return response;
    }

    @Override
    public DiceSimulation createAndSave(@Min(value = 1) int numberOfDices, @Min(value = 4) int sidesOfDice,
                                        @Min(value = 1) int numberOfRolls) {
        return repository.save(createDiceSimulation(numberOfDices, sidesOfDice, numberOfRolls));
    }

    @Override
    public DiceSimulation createDiceSimulation(int numberOfDices, int sidesOfDice, int numberOfRolls) {
        log.info("Rolling dice simulation started for numberOfDices : {} sidesOfDice : {} and numberOfRolls {}",
                numberOfDices, sidesOfDice, numberOfRolls);

        final DiceSimulation diceSimulation = DiceSimulation.builder()
                .numberOfDices(numberOfDices)
                .numberOfRolls(numberOfRolls)
                .sidesOfDice(sidesOfDice)
                .build();

        final Map<Integer, Long> relativeDistributions = IntStream.rangeClosed(1, numberOfRolls)
                .map(i -> RANDOM.nextInt(numberOfDices * sidesOfDice) + 1).boxed()
                // Group all integers together
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));// Keep key/value pairs whose value > 1

        relativeDistributions.forEach((k, v) -> log.debug("For outcome : " + k + " count was :" + v));
        diceSimulation.setRelativeDistributions(relativeDistributions);
        return diceSimulation;
    }
}
