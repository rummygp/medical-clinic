package com.rummgp.medical_clinic.validator;

import com.rummgp.medical_clinic.model.Doctor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DoctorValidator {
    public static void validateDoctorCreate(Doctor doctor) {
        if (doctor.getFirstName() == null ||
        doctor.getLastName() == null ||
        doctor.getSpecialization() == null) {
            throw new IllegalArgumentException("Fields should not be null");
        }
    }

    public static void validateDoctorUpdate(Doctor updatedDoctor) {
        if (updatedDoctor.getFirstName() == null ||
        updatedDoctor.getLastName() == null ||
        updatedDoctor.getSpecialization() == null) {
            throw new IllegalArgumentException("Fields should not be null");
        }
    }
}
