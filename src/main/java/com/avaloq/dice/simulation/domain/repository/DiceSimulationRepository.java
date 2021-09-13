package com.avaloq.dice.simulation.domain.repository;

import com.avaloq.dice.simulation.domain.entity.DiceSimulation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DiceSimulationRepository extends MongoRepository<DiceSimulation, String> {

    Optional<List<DiceSimulation>> findByNumberOfDicesAndSidesOfDice(int numberOfDices, int sidesOfDice);
}