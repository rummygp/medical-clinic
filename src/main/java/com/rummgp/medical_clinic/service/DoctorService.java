package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.exception.notFound.DoctorNotFoundException;
import com.rummgp.medical_clinic.exception.notFound.InstitutionNotFoundException;
import com.rummgp.medical_clinic.exception.notFound.UserNotFoundException;
import com.rummgp.medical_clinic.model.Doctor;
import com.rummgp.medical_clinic.model.Institution;
import com.rummgp.medical_clinic.repository.DoctorRepository;
import com.rummgp.medical_clinic.repository.InstitutionRepository;
import com.rummgp.medical_clinic.repository.UserRepository;
import com.rummgp.medical_clinic.validator.DoctorValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public Doctor find(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }

    @Transactional
    public Doctor add(Doctor doctor) {
        DoctorValidator.validateDoctorCreate(doctor);
        assignUserToDoctor(doctor);
        return doctorRepository.save(doctor);
    }

    @Transactional
    public void delete(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
        doctorRepository.delete(doctor);
    }

    @Transactional
    public Doctor update(Long id, Doctor updatedDoctor) {
        DoctorValidator.validateDoctorUpdate(updatedDoctor);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
        doctor.edit(updatedDoctor);
        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor assignInstitutionToDoctor(Long doctorId, Long institutionId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new InstitutionNotFoundException(institutionId));
        doctor.getInstitutions().add(institution);
        return doctorRepository.save(doctor);
    }

    @Transactional
    private void assignUserToDoctor(Doctor doctor) {
        if (doctor.getUser().getId() != null) {
            doctor.setUser(userRepository.findById(doctor.getUser().getId())
                    .orElseThrow(() -> new UserNotFoundException(doctor.getUser().getId())));
        }
    }
}
