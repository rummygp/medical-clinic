package com.rummgp.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends ClinicException{
    public UsernameAlreadyExistsException(String username) {
        super("Username " + username + " is already taken", HttpStatus.CONFLICT);
    }
}
