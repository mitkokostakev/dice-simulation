package com.avaloq.dice.simulation.exception;

import com.avaloq.dice.simulation.exception.dto.ErrorDto;
import com.avaloq.dice.simulation.exception.dto.ErrorsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public void handleNotFound(ResourceNotFoundException e) {
        log.error("Resource not found.", e);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorsDto handleConstraintViolations(ConstraintViolationException e) {
        final List<ErrorDto> errors = e.getConstraintViolations()
                .stream()
                .map(v -> ErrorDto.builder()
                        .message(v.getMessage())
                        .path(v.getPropertyPath().toString())
                        .value(v.getInvalidValue())
                        .build())
                .collect(toList());

        return new ErrorsDto(errors);
    }

}
