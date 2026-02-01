package com.rummgp.Exception;

public class AppointmentBookingException extends ClinicException {
    public AppointmentBookingException(String message) {
        super(message, 409);
    }
}
