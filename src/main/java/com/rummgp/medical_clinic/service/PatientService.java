package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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

    public Patient addPatient(Patient patient) {
        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Pacjent o emailu: " + patient.getEmail() + " już istnieje");
        }
        return patientRepository.add(patient);
    }

    public void removePatient(String email) {
        boolean removed = patientRepository.remove(email);
        if (!removed) {
            throw new IllegalArgumentException("Nie ma pacjenta z emailem: " + email);
        }
    }

    public Patient editPatient(String email, Patient updatedpatient) {
        if (patientRepository.findByEmail(email).isEmpty() ||
                (!email.equals(updatedpatient.getEmail()) && patientRepository.findByEmail(updatedpatient.getEmail()).isPresent())) {
                    throw new NoSuchElementException("Nieprawidłowe dane wprowadzonego użytkownika");
        }
        return patientRepository.edit(email, updatedpatient);
    }
}
