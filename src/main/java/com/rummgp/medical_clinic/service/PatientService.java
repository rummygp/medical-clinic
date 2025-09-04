package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.exception.notFound.PatientNotFoundException;
import com.rummgp.medical_clinic.exception.notFound.UserNotFoundException;
import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.repository.PatientRepository;
import com.rummgp.medical_clinic.repository.UserRepository;
import com.rummgp.medical_clinic.validator.PatientValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public Patient find(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @Transactional
    public Patient add(Patient patient) {
        PatientValidator.validatePatientCreate(patient, patientRepository);
        assignUserToPatient(patient);
        return patientRepository.save(patient);
    }

    public void delete(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        patientRepository.delete(patient);
    }

    public Patient edit(Long id, Patient updatedpatient) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        PatientValidator.validatePatientEdit(patient, updatedpatient, patientRepository);
        patient.edit(updatedpatient);
        return patientRepository.save(patient);
    }

    private void assignUserToPatient(Patient patient) {
        if (patient.getUser().getId() != null) {
            patient.setUser(userRepository.findById(patient.getUser().getId())
                    .orElseThrow(() -> new UserNotFoundException(patient.getUser().getId())));
        }
    }
}
