package com.rummgp.exception;

public class AppointmentBookingException extends ClinicException {
    public AppointmentBookingException(String message) {
        super(message, 409);
    }
}
