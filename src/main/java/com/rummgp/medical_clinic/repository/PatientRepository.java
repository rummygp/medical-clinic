package com.rummgp.medical_clinic.repository;

import com.rummgp.medical_clinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
