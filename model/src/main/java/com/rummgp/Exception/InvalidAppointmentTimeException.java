package com.rummgp.Exception;

public class InvalidAppointmentTimeException extends ClinicException {
    public InvalidAppointmentTimeException(String message) {
        super(message, 400);
    }
}
