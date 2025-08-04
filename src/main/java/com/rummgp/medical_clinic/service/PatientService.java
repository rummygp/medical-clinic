package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.repository.PatientRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("Nie ma takiego pacjenta"));
    }

    public void addPatient(Patient patient) {
        boolean added = patientRepository.add(patient);
        if (!added) {
            throw new IllegalArgumentException("Email: " + patient.getEmail() + " jest już zajęty");
        }
    }

    public void removePatient(String email) {
        boolean removed = patientRepository.remove(email);
        if (!removed) {
            throw new IllegalArgumentException("Nie ma pacjenta z emailem: " + email);
        }
    }

    public void editPatient(String email, Patient updatedpatient) {
        boolean edited = patientRepository.edit(email, updatedpatient);
        if (!edited) {
            if (patientRepository.findByEmail(email).isEmpty()) {
                throw new IllegalArgumentException("Nie ma pacjenta z emailem: " + email);
            } else {
                throw new IllegalArgumentException("Email: " + email + " jest już zajęty");
            }
        }
    }
}
