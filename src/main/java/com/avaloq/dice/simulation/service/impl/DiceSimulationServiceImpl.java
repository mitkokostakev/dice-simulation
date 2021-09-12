package com.avaloq.dice.simulation.service.impl;

import com.avaloq.dice.simulation.domain.DiceSimulationResponse;
import com.avaloq.dice.simulation.service.DiceSimulationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Min;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiceSimulationServiceImpl implements DiceSimulationService {

    private static final Random RANDOM = new Random();

    @Override
    public DiceSimulationResponse create(@Min(value = 1) int numberOfDices, @Min(value = 4) int sidesOfDice,
                                         @Min(value = 1) int numberOfRolls) {
        log.info("Rolling dice simulation started for numberOfDices : {} sidesOfDice : {} and numberOfRolls {}",
                numberOfDices, sidesOfDice, numberOfRolls);

        final DiceSimulationResponse diceSimulationResponse = DiceSimulationResponse.builder()
                .numberOfDices(numberOfDices)
                .numberOfRolls(numberOfRolls)
                .sidesOfDice(sidesOfDice)
                .build();

        final Map<Integer, Long> relativeDistributions = IntStream.rangeClosed(1, numberOfRolls)
                .map(i -> RANDOM.nextInt(numberOfDices * sidesOfDice) + 1).boxed()
                // Group all integers together
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()));// Keep key/value pairs whose value > 1

        relativeDistributions.forEach((k, v) -> log.debug("For outcome : " + k + " count was :" + v));
        diceSimulationResponse.setRelativeDistributions(relativeDistributions);
        return diceSimulationResponse;
    }
}