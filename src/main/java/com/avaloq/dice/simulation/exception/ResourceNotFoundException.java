package com.avaloq.dice.simulation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 4564564564564L;

    public ResourceNotFoundException() {
        super("Resource does not exist...");
    }

    public ResourceNotFoundException(String exception) {
        super(exception);
    }
}