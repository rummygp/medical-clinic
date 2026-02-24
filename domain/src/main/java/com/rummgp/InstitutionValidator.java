package com.rummgp;

import com.rummgp.exception.FieldsShouldNotBeNullException;
import com.rummgp.exception.NameAlreadyExistsException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InstitutionValidator {

    public static void validateInstitutionCreate(Institution institution, InstitutionRepositoryPort institutionRepositoryPort) {
        if (institution.getName() == null ||
                institution.getCity() == null ||
                institution.getPostalCode() == null ||
                institution.getStreet() == null ||
                institution.getBuildingNo() == null) {
            throw new FieldsShouldNotBeNullException();
        }
        if (institutionRepositoryPort.findByName(institution.getName()).isPresent()) {
            throw new NameAlreadyExistsException(institution.getName());
        }
    }

    public static void validateInstitutionUpdate(Institution updatedInstitution, InstitutionRepositoryPort institutionRepositoryPort) {
        if (updatedInstitution.getName() == null ||
                updatedInstitution.getCity() == null ||
                updatedInstitution.getPostalCode() == null ||
                updatedInstitution.getStreet() == null ||
                updatedInstitution.getBuildingNo() == null) {
            throw new FieldsShouldNotBeNullException();
        }
        if (institutionRepositoryPort.findByName(updatedInstitution.getName()).isPresent()) {
            throw new NameAlreadyExistsException(updatedInstitution.getName());
        }
    }
}
