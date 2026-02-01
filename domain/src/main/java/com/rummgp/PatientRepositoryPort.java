package com.rummgp;

import java.util.Optional;

public interface PatientRepositoryPort {

    Optional<Patient> findById(Long id);
}
