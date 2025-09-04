package com.rummgp.medical_clinic.exception.notFound;

import com.rummgp.medical_clinic.exception.ClinicException;
import org.springframework.http.HttpStatus;

public class PatientNotFoundException extends ClinicException {

    public PatientNotFoundException(Long id) {
        super("Patient with id: " + id + " doesn't exist", HttpStatus.NOT_FOUND);
    }
}
