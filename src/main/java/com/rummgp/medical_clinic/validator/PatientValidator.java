package com.rummgp.medical_clinic.validator;

import com.rummgp.medical_clinic.exception.FieldsShouldNotBeNullException;
import com.rummgp.medical_clinic.exception.ImmutableFieldException;
import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.repository.PatientRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PatientValidator {

    public static void validatePatientCreate(Patient patient, PatientRepository patientRepository) {
        if (patient.getFirstName() == null ||
                patient.getLastName() == null ||
                patient.getIdCardNo() == null ||
                patient.getPhoneNumber() == null ||
                patient.getBirthday() == null) {
            throw new FieldsShouldNotBeNullException();
        }
    }

    public static void validatePatientEdit(Patient patient, Patient updatedpatient, PatientRepository patientRepository) {
        if (updatedpatient.getFirstName() == null ||
                updatedpatient.getLastName() == null ||
                updatedpatient.getIdCardNo() == null ||
                updatedpatient.getPhoneNumber() == null ||
                updatedpatient.getBirthday() == null) {
            throw new FieldsShouldNotBeNullException();
        }
        if (!patient.getIdCardNo().equals(updatedpatient.getIdCardNo())) {
            throw new ImmutableFieldException("idCardNo");
        }
    }
}
