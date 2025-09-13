package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.dto.PatientDto;
import com.rummgp.medical_clinic.exception.NotFoundException;
import com.rummgp.medical_clinic.mapper.PageMapper;
import com.rummgp.medical_clinic.mapper.PatientMapper;
import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.repository.PatientRepository;
import com.rummgp.medical_clinic.repository.UserRepository;
import com.rummgp.medical_clinic.validator.PatientValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;
    private final PageMapper pageMapper;

    public PageDto<PatientDto> findAll(Pageable pageable) {
        return pageMapper.toDto(patientRepository.findAll(pageable), patientMapper::toDto);
    }

    public Patient find(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient", id));
    }

    @Transactional
    public Patient add(Patient patient) {
        PatientValidator.validatePatientCreate(patient, patientRepository);
        assignUserToPatient(patient);
        return patientRepository.save(patient);
    }

    @Transactional
    public void delete(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient",id));
        patientRepository.delete(patient);
    }

    @Transactional
    public Patient edit(Long id, Patient updatedpatient) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient",id));
        PatientValidator.validatePatientEdit(patient, updatedpatient, patientRepository);
        patient.edit(updatedpatient);
        return patientRepository.save(patient);
    }

    private void assignUserToPatient(Patient patient) {
        if (patient.getUser().getId() != null) {
            patient.setUser(userRepository.findById(patient.getUser().getId())
                    .orElseThrow(() -> new NotFoundException("User", patient.getUser().getId())));
        }
    }
}
