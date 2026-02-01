package com.rummgp;

import java.util.Optional;

public interface DoctorRepositoryPort {

    PagePojo<Doctor> findAll(DoctorFindCommand doctorFindCommand);

    Optional<Doctor> findById(Long id);

    Doctor save(Doctor doctor);

    void delete(Doctor doctor);
}
