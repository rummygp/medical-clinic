package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.dto.DoctorDto;
import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.exception.NotFoundException;
import com.rummgp.medical_clinic.mapper.DoctorMapper;
import com.rummgp.medical_clinic.model.Doctor;
import com.rummgp.medical_clinic.model.Institution;
import com.rummgp.medical_clinic.repository.DoctorRepository;
import com.rummgp.medical_clinic.repository.InstitutionRepository;
import com.rummgp.medical_clinic.repository.UserRepository;
import com.rummgp.medical_clinic.validator.DoctorValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final DoctorMapper doctorMapper;

    public PageDto<DoctorDto> findAll(Pageable pageable) {
        Page<Doctor> page;

        page = doctorRepository.findAll(pageable);
        return new PageDto<>(
                page.map(doctorMapper::toDto).getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    public Doctor find(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor", id));
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
                .orElseThrow(() -> new NotFoundException("Doctor", id));
        doctorRepository.delete(doctor);
    }

    @Transactional
    public Doctor update(Long id, Doctor updatedDoctor) {
        DoctorValidator.validateDoctorUpdate(updatedDoctor);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor", id));
        doctor.edit(updatedDoctor);
        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor assignInstitutionToDoctor(Long doctorId, Long institutionId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Doctor", doctorId));
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new NotFoundException("Institution", institutionId));
        doctor.getInstitutions().add(institution);
        return doctorRepository.save(doctor);
    }

    @Transactional
    private void assignUserToDoctor(Doctor doctor) {
        if (doctor.getUser().getId() != null) {
            doctor.setUser(userRepository.findById(doctor.getUser().getId())
                    .orElseThrow(() -> new NotFoundException("User", doctor.getUser().getId())));
        }
    }
}
