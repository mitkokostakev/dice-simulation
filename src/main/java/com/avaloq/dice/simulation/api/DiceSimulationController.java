package com.avaloq.dice.simulation.api;

import com.avaloq.dice.simulation.domain.DiceSimulationResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/simulations")
@Api(value = "api/v1/simulations")
@Validated
public interface DiceSimulationController {

    // Swagger info section-------------------------------------------------
    @ApiOperation(
            value = "Create a new Rolling Dice Simulation",
            response = DiceSimulationResponse.class,
            tags = {})
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 201,
                            message = "Successfully created a new Rolling Dice Simulation",
                            response = DiceSimulationResponse.class),
                    @ApiResponse(code = 400, message = "Bad request")
            })
    // Swagger info section-------------------------------------------------
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    DiceSimulationResponse create(@RequestParam @Min(value = 1) int numberOfDices,
                                  @RequestParam @Min(value = 4) int sidesOfDice,
                                  @RequestParam @Min(value = 1) int numberOfRolls);
}
