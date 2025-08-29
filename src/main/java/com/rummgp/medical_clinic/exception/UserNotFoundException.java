package com.rummgp.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ClinicException{
    public UserNotFoundException(Long id) {
        super("User with id: " + id + " doesn't exist", HttpStatus.NOT_FOUND);
    }
}
