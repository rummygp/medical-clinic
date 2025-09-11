package com.rummgp.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class InvalidAppointmentTimeException extends ClinicException {
    public InvalidAppointmentTimeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
