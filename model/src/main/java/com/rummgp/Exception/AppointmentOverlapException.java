package com.rummgp.Exception;

public class AppointmentOverlapException extends ClinicException {
    public AppointmentOverlapException(String message) {
        super(message, 409);
    }
}
