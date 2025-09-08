package com.rummgp.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class AppointmentExpiredException extends ClinicException {
    public AppointmentExpiredException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
