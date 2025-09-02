package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.exception.PatientNotFoundException;
import com.rummgp.medical_clinic.exception.UserNotFoundException;
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

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    public Patient getPatient(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }

    @Transactional
    public Patient addPatient(Patient patient) {
        PatientValidator.validatePatientCreate(patient, patientRepository);
        if (patient.getUser().getId() != null) {
            patient.setUser(userRepository.findById(patient.getUser().getId())
                    .orElseThrow(() -> new UserNotFoundException(patient.getUser().getId())));
        }
        return patientRepository.save(patient);
    }

    public void removePatient(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
        patientRepository.delete(patient);
        }

    public Patient editPatient(String email, Patient updatedpatient) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
        PatientValidator.validatePatientEdit(patient, updatedpatient, patientRepository);
        patient.edit(updatedpatient);
        return patientRepository.save(patient);
    }
}
