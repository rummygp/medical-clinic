package com.rummgp.medical_clinic.validator;

import com.rummgp.medical_clinic.exception.EmailAlreadyExistsException;
import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.repository.PatientRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PatientValidator {
        public static void validatePatientCreate(Patient patient, PatientRepository patientRepository) {
            if (patient.getFirstName() == null ||
                    patient.getLastName() == null||
                    patient.getEmail() == null ||
                    patient.getIdCardNo() == null ||
                    patient.getPhoneNumber() == null ||
                    patient.getBirthday() == null) {
                throw new IllegalArgumentException("Fields should not be null");
            }
            if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException(patient.getEmail());
            }
        }

        public static void validatePatientEdit(Patient patient, Patient updatedpatient, PatientRepository patientRepository) {
            if (updatedpatient.getFirstName() == null ||
                    updatedpatient.getLastName() == null||
                    updatedpatient.getEmail() == null ||
                    updatedpatient.getIdCardNo() == null ||
                    updatedpatient.getPhoneNumber() == null ||
                    updatedpatient.getBirthday() == null) {
                throw new IllegalArgumentException("Fields should not be null");
            }
            if (!patient.getIdCardNo().equals(updatedpatient.getIdCardNo())) {
                throw new IllegalArgumentException("Id Card number can't be changed");
            }
            if ((!patient.getEmail().equals(updatedpatient.getEmail()) && patientRepository.findByEmail(updatedpatient.getEmail()).isPresent())) {
                throw new EmailAlreadyExistsException(updatedpatient.getEmail());
            }
        }
}
