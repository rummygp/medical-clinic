package com.rummgp.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class NameAlreadyExistsException extends ClinicException {
    public NameAlreadyExistsException(String name) {
        super("Institution with name: " + name + " already exist", HttpStatus.CONFLICT);
    }
}
