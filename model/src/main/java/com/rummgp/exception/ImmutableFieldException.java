package com.rummgp.exception;

public class ImmutableFieldException extends ClinicException {

    public ImmutableFieldException(String fieldName) {
        super(fieldName + " field can't be changed", 409);
    }
}
