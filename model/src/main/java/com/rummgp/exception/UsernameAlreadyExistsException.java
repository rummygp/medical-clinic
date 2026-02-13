package com.rummgp.exception;

public class UsernameAlreadyExistsException extends ClinicException {

    public UsernameAlreadyExistsException(String username) {
        super("Username " + username + " is already taken", 409);
    }
}
