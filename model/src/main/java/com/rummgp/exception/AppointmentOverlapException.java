package com.rummgp.exception;

public class AppointmentOverlapException extends ClinicException {
    public AppointmentOverlapException(String message) {
        super(message, 409);
    }
}
