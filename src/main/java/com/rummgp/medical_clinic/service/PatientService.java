package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.exception.PatientNotFoundException;
import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.repository.PatientRepository;
import com.rummgp.medical_clinic.validator.PatientValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    public Patient getPatient(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
    }

    public Patient addPatient(Patient patient) {
        PatientValidator.validatePatientCreate(patient, patientRepository);
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
        return patientRepository.save(patient.edit(updatedpatient));
    }

    public Patient changePassword (String email, String password) {
        PatientValidator.validatePasswordEdit(password);
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException(email));
        patient.setPassword(password);
        return patientRepository.save(patient);
    }
}
