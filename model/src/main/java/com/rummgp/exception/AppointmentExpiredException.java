package com.rummgp.exception;

public class AppointmentExpiredException extends ClinicException {
    public AppointmentExpiredException(String message) {
        super(message, 400);
    }
}
