package com.rummgp.medical_clinic.repository;

import com.rummgp.medical_clinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);

    void deleteByEmail(String email);
}
