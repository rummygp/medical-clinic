package com.rummgp;

import com.rummgp.exception.FieldsShouldNotBeNullException;
import com.rummgp.exception.ImmutableFieldException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PatientValidator {

    public static void validatePatientCreate(Patient patient) {
        if (patient.getFirstName() == null ||
                patient.getLastName() == null ||
                patient.getIdCardNo() == null ||
                patient.getPhoneNumber() == null ||
                patient.getBirthday() == null) {
            throw new FieldsShouldNotBeNullException();
        }
    }

    public static void validatePatientEdit(Patient patient, Patient updatedpatient) {
        if (updatedpatient.getFirstName() == null ||
                updatedpatient.getLastName() == null ||
                updatedpatient.getPhoneNumber() == null ||
                updatedpatient.getBirthday() == null) {
            throw new FieldsShouldNotBeNullException();
        }
        if (!patient.getIdCardNo().equals(updatedpatient.getIdCardNo())) {
            throw new ImmutableFieldException("idCardNo");
        }
    }
}
