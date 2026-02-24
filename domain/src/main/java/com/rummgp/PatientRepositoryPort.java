package com.rummgp;

import java.util.Optional;

public interface PatientRepositoryPort {

    PagePojo<Patient> findAll(PatientFindCommand patientFindCommand);

    Optional<Patient> findById(Long id);

    Patient save(Patient patient);

    void delete(Patient patient);
}
