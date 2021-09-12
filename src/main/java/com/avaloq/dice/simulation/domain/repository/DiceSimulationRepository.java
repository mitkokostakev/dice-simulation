package com.avaloq.dice.simulation.domain.repository;

import com.avaloq.dice.simulation.domain.entity.DiceSimulation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiceSimulationRepository extends MongoRepository<DiceSimulation, String> {

}