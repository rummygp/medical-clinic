package com.rummgp.medical_clinic.validator;

import com.rummgp.medical_clinic.exception.NameAlreadyExistsException;
import com.rummgp.medical_clinic.model.Institution;
import com.rummgp.medical_clinic.repository.InstitutionRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InstitutionValidator {
    public static void validateInstitutionCreate(Institution institution, InstitutionRepository institutionRepository) {
        if (institution.getName() == null ||
                institution.getCity() == null ||
                institution.getPostalCode() == null ||
                institution.getStreet() == null ||
                institution.getBuildingNo() == null) {
            throw new IllegalArgumentException("Fields should not be null");
        }
        if (institutionRepository.findByName(institution.getName()).isPresent()) {
            throw new NameAlreadyExistsException(institution.getName());
        }
    }

    public static void validateInstitutionUpdate(Institution updatedInstitution, InstitutionRepository institutionRepository) {
        if (updatedInstitution.getName() == null ||
                updatedInstitution.getCity() == null ||
                updatedInstitution.getPostalCode() == null ||
                updatedInstitution.getStreet() == null ||
                updatedInstitution.getBuildingNo() == null) {
            throw new IllegalArgumentException("Fields should not be null");
        }
        if (institutionRepository.findByName(updatedInstitution.getName()).isPresent()) {
            throw new NameAlreadyExistsException(updatedInstitution.getName());
        }
    }
}
