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
import com.rummgp.medical_clinic.validator.AppointmentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;
    private final PageMapper pageMapper;

    public PageDto<AppointmentDto> find(Long doctorId, Long patientId, Pageable pageable) {
        Page<Appointment> page;

        if (doctorId != null && patientId != null) {
            page = appointmentRepository.findByDoctorIdAndPatientId(doctorId, patientId, pageable);
        } else if (doctorId != null) {
            page = appointmentRepository.findByDoctorId(doctorId, pageable);
        } else if (patientId != null) {
            page = appointmentRepository.findByPatientId(patientId, pageable);
        } else {
            page = appointmentRepository.findAll(pageable);
        }
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
}
