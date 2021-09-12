package com.avaloq.dice.simulation.domain.mapper;

import com.avaloq.dice.simulation.domain.dto.DiceSimulationResponse;
import com.avaloq.dice.simulation.domain.entity.DiceSimulation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiceSimulationMapper {

    DiceSimulationResponse mapFromEntity(DiceSimulation diceSimulation);

}
