package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.exception.notFound.InstitutionNotFoundException;
import com.rummgp.medical_clinic.model.Institution;
import com.rummgp.medical_clinic.repository.InstitutionRepository;
import com.rummgp.medical_clinic.validator.InstitutionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InstitutionService {
    private final InstitutionRepository institutionRepository;

    public List<Institution> getAll() {
        return institutionRepository.findAll();
    }

    public Institution getInstitution(Long id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new InstitutionNotFoundException(id));
    }

    public Institution addInstitution(Institution institution) {
        InstitutionValidator.validateInstitutionCreate(institution, institutionRepository);
        return institutionRepository.save(institution);
    }

    public void removeInstitution(Long id) {
        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new InstitutionNotFoundException(id));
        institutionRepository.delete(institution);
    }

    public Institution updateInstitution(Long id, Institution updatedInstitution) {
        InstitutionValidator.validateInstitutionUpdate(updatedInstitution, institutionRepository);
        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new InstitutionNotFoundException(id));
        institution.edit(updatedInstitution);
        return institutionRepository.save(institution);
    }
}
