package com.rummgp.exception;

import lombok.Getter;

@Getter
public class ClinicException extends RuntimeException {
    private final Integer status;

    public ClinicException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
