package com.rummgp.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends ClinicException{
    public EmailAlreadyExistsException(String email) {
        super("Patient with email: " + email + " already exists", HttpStatus.CONFLICT);
    }
}
