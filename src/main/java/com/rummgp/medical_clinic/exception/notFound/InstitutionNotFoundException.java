package com.rummgp.medical_clinic.exception.notFound;

import com.rummgp.medical_clinic.exception.ClinicException;
import org.springframework.http.HttpStatus;

public class InstitutionNotFoundException extends ClinicException {
    public InstitutionNotFoundException(Long id) {
        super("Institution with id: " + id + " doesn't exist", HttpStatus.NOT_FOUND);
    }
}
