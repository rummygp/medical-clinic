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

    public boolean add(Patient patient) {
        if (findByEmail(patient.getEmail()).isPresent()) {
            return false;
        }
        patients.add(patient);
        return true;
    }

    public boolean remove(String email) {
        return patients.removeIf(patient -> patient.getEmail().equals(email));
    }

    public boolean edit(String email, Patient updated) {
        var opt = findByEmail(email);
        if (opt.isEmpty()) return false;

        if (!email.equals(updated.getEmail()) && findByEmail(updated.getEmail()).isPresent()) {
            return false;
        }

        Patient current = opt.get();

        current.setFirstName(updated.getFirstName());
        current.setLastName(updated.getLastName());
        current.setEmail(updated.getEmail());
        current.setPhoneNumber(updated.getPhoneNumber());
        current.setBirthday(updated.getBirthday());
        current.setIdCardNo(updated.getIdCardNo());
        current.setPassword(updated.getPassword());

        return true;
    }
}
