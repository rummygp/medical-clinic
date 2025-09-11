package com.rummgp.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class AppointmentOverlapException extends ClinicException {
    public AppointmentOverlapException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
