package com.rummgp;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PatientRepositoryProvider implements PatientRepositoryPort {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PageMapper pageMapper;

    @Override
    public PagePojo<Patient> findAll(PatientFindCommand patientFindCommand) {
        Pageable pageable = PageRequest.of(patientFindCommand.pageNumber(), patientFindCommand.pageSize());
        Page<PatientEntity> page = patientRepository.findAll(pageable);
        return pageMapper.toPojo(page, patientMapper::toPojo);
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id)
                .map(patientMapper::toPojo);
    }

    @Override
    public void delete(Patient patient) {
        patientRepository.delete(patientMapper.toEntity(patient));
    }

    @Override
    public Patient save(Patient patient) {
        PatientEntity patientEntity = patientMapper.toEntity(patient);
        return patientMapper.toPojo(patientRepository.save(patientEntity));
    }
}
