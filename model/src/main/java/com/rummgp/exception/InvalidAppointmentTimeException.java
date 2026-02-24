package com.rummgp.exception;

public class InvalidAppointmentTimeException extends ClinicException {
    public InvalidAppointmentTimeException(String message) {
        super(message, 400);
    }
}
