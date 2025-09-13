package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.dto.InstitutionDto;
import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.exception.NotFoundException;
import com.rummgp.medical_clinic.mapper.InstitutionMapper;
import com.rummgp.medical_clinic.mapper.PageMapper;
import com.rummgp.medical_clinic.model.Institution;
import com.rummgp.medical_clinic.repository.InstitutionRepository;
import com.rummgp.medical_clinic.validator.InstitutionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InstitutionService {
    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;
    private final PageMapper pageMapper;

    public PageDto<InstitutionDto> findAll(Pageable pageable) {
        return pageMapper.toDto(institutionRepository.findAll(pageable), institutionMapper::toDto);
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
