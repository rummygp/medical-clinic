package com.rummgp.exception;


public class NameAlreadyExistsException extends ClinicException {

    public NameAlreadyExistsException(String name) {
        super("Institution with name: " + name + " already exist", 409);
    }
}
