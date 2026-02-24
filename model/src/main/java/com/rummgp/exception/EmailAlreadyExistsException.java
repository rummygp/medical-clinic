package com.rummgp.exception;



public class EmailAlreadyExistsException extends ClinicException {

    public EmailAlreadyExistsException(String email) {
        super("Patient with email: " + email + " already exists", 409);
    }
}
