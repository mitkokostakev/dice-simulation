package com.avaloq.dice.simulation.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class DiceSimulation {

    @Id
    private String id;

    @Min(value = 1)
    private int numberOfDices;

    @Min(value = 4)
    private int sidesOfDice;

    @Min(value = 1)
    private int numberOfRolls;

    private Map<Integer, Long> relativeDistributions;
}
