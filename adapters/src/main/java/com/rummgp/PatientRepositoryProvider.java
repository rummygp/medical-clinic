package com.rummgp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PatientRepositoryProvider implements PatientRepositoryPort {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id)
                .map(patientMapper::toPojo);
    }
}
