package com.rummgp.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class ImmutableFieldException extends ClinicException {

    public ImmutableFieldException(String fieldName) {
        super(fieldName + " field can't be changed", HttpStatus.CONFLICT);
    }
}
