package com.rummgp.medical_clinic.mapper;

import com.rummgp.medical_clinic.dto.PatientDto;
import com.rummgp.medical_clinic.model.Patient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PatientMapper {

    public static PatientDto toDto(Patient patient) {
        return new PatientDto(
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getPhoneNumber(),
                patient.getBirthday()
        );
    }
}
