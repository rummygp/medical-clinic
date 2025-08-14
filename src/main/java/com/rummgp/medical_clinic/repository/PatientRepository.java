package com.rummgp.medical_clinic.repository;

import com.rummgp.medical_clinic.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepository {
    private List<Patient> patients = new ArrayList<>();

    public List<Patient> findAll() {
        return new ArrayList<>(patients);
    }

    public Optional<Patient> findByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }

    public Patient add(Patient patient) {
        patients.add(patient);
        return patient;
    }

    public boolean remove(String email) {
        return patients.removeIf(patient -> patient.getEmail().equals(email));
    }

    public Patient edit(Patient patient, Patient updatedPatient) {
        return patient.edit(updatedPatient);
    }
}
