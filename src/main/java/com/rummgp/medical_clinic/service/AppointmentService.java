package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.command.AppointmentCreateCommand;
import com.rummgp.medical_clinic.dto.AppointmentDto;
import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.exception.NotFoundException;
import com.rummgp.medical_clinic.mapper.AppointmentMapper;
import com.rummgp.medical_clinic.mapper.PageMapper;
import com.rummgp.medical_clinic.model.Appointment;
import com.rummgp.medical_clinic.model.Doctor;
import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.repository.AppointmentRepository;
import com.rummgp.medical_clinic.repository.DoctorRepository;
import com.rummgp.medical_clinic.repository.PatientRepository;
import com.rummgp.medical_clinic.repository.specification.AppointmentSpecification;
import com.rummgp.medical_clinic.validator.AppointmentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;
    private final PageMapper pageMapper;

    public PageDto<AppointmentDto> find(Long doctorId, Long patientId, String specialization,
                                        LocalDateTime intervalStart, LocalDateTime intervalEnd, Boolean freeSlots, Pageable pageable) {
        if (doctorId != null) {
            doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new NotFoundException("Doctor", doctorId));
        }
        if (patientId != null) {
            patientRepository.findById(patientId)
                    .orElseThrow(() -> new NotFoundException("Patient", patientId));
        }
        Specification<Appointment> spec = AppointmentSpecification.filter(doctorId, patientId, specialization, intervalStart, intervalEnd, freeSlots);
        Page<Appointment> page = appointmentRepository.findAll(spec, pageable);
        return pageMapper.toDto(page, appointmentMapper::toDto);
    }

    public Appointment add(AppointmentCreateCommand appointment) {
        Doctor doctor = doctorRepository.findById(appointment.doctorId())
                .orElseThrow(() -> new NotFoundException("Doctor", appointment.doctorId()));
        Appointment newAppointment = appointmentMapper.toEntity(appointment);
        AppointmentValidator.validateAppointmentCreate(newAppointment, appointmentRepository, doctor.getId());
        newAppointment.setDoctor(doctor);
        return appointmentRepository.save(newAppointment);
    }

    public Appointment bookAppointment(Long appointmentId, Long patientId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment", appointmentId));
        AppointmentValidator.validateBookAppointment(appointment);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("Patient", patientId));
        appointment.setPatient(patient);
        return appointmentRepository.save(appointment);
    }

    public void delete(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment", id));
        appointmentRepository.delete(appointment);
    }
}
