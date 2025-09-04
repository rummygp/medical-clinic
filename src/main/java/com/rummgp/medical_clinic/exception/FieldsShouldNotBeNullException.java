package com.rummgp.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class FieldsShouldNotBeNullException extends ClinicException {

    public FieldsShouldNotBeNullException() {
        super("Fields should not be null", HttpStatus.BAD_REQUEST);
    }
}
