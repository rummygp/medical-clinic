package com.rummgp;

import com.rummgp.Exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepositoryPort doctorRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final InstitutionRepositoryPort institutionRepositoryPort;

    public PagePojo<Doctor> find(DoctorFindCommand doctorFindCommand) {
        return doctorRepositoryPort.findAll(doctorFindCommand);
    }

    public Doctor find(Long id) {
        return doctorRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor", id));
    }

    public Doctor add(Doctor doctor) {
        DoctorValidator.validateDoctorCreate(doctor);
        assignUserToDoctor(doctor);
        return doctorRepositoryPort.save(doctor);
    }

    public void delete(Long id) {
        Doctor doctor = doctorRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor", id));
        doctorRepositoryPort.delete(doctor);
    }

    public Doctor update(Long id, Doctor updatedDoctor) {
        DoctorValidator.validateDoctorUpdate(updatedDoctor);
        Doctor doctor = doctorRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor", id));
        doctor.edit(updatedDoctor);
        return doctorRepositoryPort.save(doctor);
    }

    public Doctor assignInstitutionToDoctor(Long doctorId, Long institutionId) {
        Doctor doctor = doctorRepositoryPort.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Doctor", doctorId));
        Institution institution = institutionRepositoryPort.findById(institutionId)
                .orElseThrow(() -> new NotFoundException("Institution", institutionId));
        doctor.getInstitutions().add(institution);
        return doctorRepositoryPort.save(doctor);
    }

    private void assignUserToDoctor(Doctor doctor) {
        if (doctor.getUser().getId() != null) {
            doctor.setUser(userRepositoryPort.findById(doctor.getUser().getId())
                    .orElseThrow(() -> new NotFoundException("User", doctor.getUser().getId())));
        }
    }
}
