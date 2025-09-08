package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.exception.NotFoundException;
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

    public List<Institution> findAll() {
        return institutionRepository.findAll();
    }

    public Institution find(Long id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Institution", id));
    }

    public Institution add(Institution institution) {
        InstitutionValidator.validateInstitutionCreate(institution, institutionRepository);
        return institutionRepository.save(institution);
    }

    public void delete(Long id) {
        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Institution",id));
        institutionRepository.delete(institution);
    }

    public Institution update(Long id, Institution updatedInstitution) {
        InstitutionValidator.validateInstitutionUpdate(updatedInstitution, institutionRepository);
        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Institution",id));
        institution.edit(updatedInstitution);
        return institutionRepository.save(institution);
    }
}
