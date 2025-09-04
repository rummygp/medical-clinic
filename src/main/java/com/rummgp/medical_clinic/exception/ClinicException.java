package com.rummgp.medical_clinic.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClinicException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ClinicException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
