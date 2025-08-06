package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

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
        if (patient.getFirstName() == null ||
                patient.getLastName() == null||
                patient.getEmail() == null ||
                patient.getPassword() == null ||
                patient.getIdCardNo() == null ||
                patient.getPhoneNumber() == null ||
                patient.getBirthday() == null) {
            throw new IllegalArgumentException("Fields should not be null");
        }
        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Pacjent o emailu: " + patient.getEmail() + " już istnieje");
        }
        return patientRepository.add(patient);
    }

    public void removePatient(String email) {
        if (!patientRepository.remove(email)) {
            throw new NoSuchElementException("Pacjet o emailu: " + email + " nie istnieje.");
        }
    }

    public Patient editPatient(String email, Patient updatedpatient) {
        if (updatedpatient.getFirstName() == null ||
                updatedpatient.getLastName() == null||
                updatedpatient.getEmail() == null ||
                updatedpatient.getPassword() == null ||
                updatedpatient.getIdCardNo() == null ||
                updatedpatient.getPhoneNumber() == null ||
                updatedpatient.getBirthday() == null) {
            throw new IllegalArgumentException("Fields should not be null");
        }
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Pacjet o emailu: " + email + " nie istnieje."));
        if (!patient.getIdCardNo().equals(updatedpatient.getIdCardNo())) {
            throw new IllegalArgumentException("Id Card number can't be changed");
        }
        if ((!email.equals(updatedpatient.getEmail()) && patientRepository.findByEmail(updatedpatient.getEmail()).isPresent())) {
            throw new IllegalArgumentException("Nieprawidłowe dane wprowadzonego użytkownika");
        }
        return patientRepository.edit(patient, updatedpatient);
    }

    public Patient changePassword (String email, String password) {
        if (password == null) {
            throw new IllegalArgumentException("Fields should not be null");
        }
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Pacjet o emailu: " + email + " nie istnieje."));
        patient.setPassword(password);
        return patientRepository.edit(patient, patient);
    }
}
