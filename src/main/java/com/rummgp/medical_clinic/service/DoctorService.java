package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.exception.notFound.DoctorNotFoundException;
import com.rummgp.medical_clinic.exception.notFound.UserNotFoundException;
import com.rummgp.medical_clinic.model.Doctor;
import com.rummgp.medical_clinic.repository.DoctorRepository;
import com.rummgp.medical_clinic.repository.UserRepository;
import com.rummgp.medical_clinic.validator.DoctorValidator;
import jakarta.validation.constraints.Negative;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }

    public Doctor addDoctor(Doctor doctor) {
        DoctorValidator.validateDoctorCreate(doctor);
        if (doctor.getUser().getId() != null) {
            doctor.setUser(userRepository.findById(doctor.getUser().getId())
                    .orElseThrow(() -> new UserNotFoundException(doctor.getUser().getId())));
        }
        return doctorRepository.save(doctor);
    }

    public void removeDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
        doctorRepository.delete(doctor);
    }

    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        DoctorValidator.validateDoctorUpdate(updatedDoctor);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
        doctor.edit(updatedDoctor);
        return doctorRepository.save(doctor);
    }
}
