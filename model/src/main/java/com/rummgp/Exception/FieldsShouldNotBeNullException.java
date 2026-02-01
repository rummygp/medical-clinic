package com.rummgp.Exception;

public class FieldsShouldNotBeNullException extends ClinicException {

    public FieldsShouldNotBeNullException() {
        super("Fields should not be null", 400);
    }
}
