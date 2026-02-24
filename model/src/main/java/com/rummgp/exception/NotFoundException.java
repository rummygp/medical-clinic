package com.rummgp.exception;

public class NotFoundException extends ClinicException {

    public NotFoundException(String resource, Long id) {
        super(resource + " with id: " + id + " doesn't exist", 404);
    }
}
