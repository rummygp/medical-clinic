package com.rummgp.medical_clinic.exception;

import org.springframework.http.HttpStatus;

public class AppointmentBookingException extends ClinicException {
    public AppointmentBookingException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
