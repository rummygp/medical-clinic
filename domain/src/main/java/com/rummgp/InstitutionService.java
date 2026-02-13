package com.rummgp;

import com.rummgp.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InstitutionService {
    private final InstitutionRepositoryPort institutionRepositoryPort;

    public PagePojo<Institution> findAll(InstitutionFindCommand institutionFindCommand) {
        return institutionRepositoryPort.findAll(institutionFindCommand);
    }

    public Institution find(Long id) {
        return institutionRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Institution", id));
    }

    public Institution add(Institution institution) {
        InstitutionValidator.validateInstitutionCreate(institution, institutionRepositoryPort);
        return institutionRepositoryPort.save(institution);
    }

    public void delete(Long id) {
        Institution institution = institutionRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Institution",id));
        institutionRepositoryPort.delete(institution);
    }

    public Institution update(Long id, Institution updatedInstitution) {
        InstitutionValidator.validateInstitutionUpdate(updatedInstitution, institutionRepositoryPort);
        Institution institution = institutionRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Institution",id));
        institution.edit(updatedInstitution);
        return institutionRepositoryPort.save(institution);
    }
}
