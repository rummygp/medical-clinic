package com.rummgp.medical_clinic.validator;

import com.rummgp.medical_clinic.model.Institution;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InstitutionValidator {
    public static void validateInstitutionCreate(Institution institution) {
        if (institution.getName() == null ||
                institution.getCity() == null ||
                institution.getPostalCode() == null ||
                institution.getStreet() == null ||
                institution.getBuildingNo() == null) {
            throw new IllegalArgumentException("Fields should not be null");
        }
    }

    public static void validateInstitutionUpdate(Institution updatedInstitution) {
        if (updatedInstitution.getName() == null ||
                updatedInstitution.getCity() == null ||
                updatedInstitution.getPostalCode() == null ||
                updatedInstitution.getStreet() == null ||
                updatedInstitution.getBuildingNo() == null) {
            throw new IllegalArgumentException("Fields should not be null");
        }
    }
}
