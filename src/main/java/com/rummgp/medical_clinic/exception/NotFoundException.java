package com.rummgp.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ClinicException {

    public NotFoundException(String resource, Long id) {
        super(resource + " with id: " + id + " doesn't exist", HttpStatus.NOT_FOUND);
    }
}
