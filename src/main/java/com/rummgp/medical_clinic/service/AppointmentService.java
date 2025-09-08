package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.exception.NotFoundException;
import com.rummgp.medical_clinic.model.Appointment;
import com.rummgp.medical_clinic.model.Doctor;
import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.repository.AppointmentRepository;
import com.rummgp.medical_clinic.repository.DoctorRepository;
import com.rummgp.medical_clinic.repository.PatientRepository;
import com.rummgp.medical_clinic.validator.AppointmentValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Transactional
    public Appointment addEmpty(Long doctorId, Appointment appointment) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Doctor", doctorId));
        AppointmentValidator.validateAppointmentCreate(appointment, appointmentRepository, doctorId);
        appointment.setDoctor(doctor);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment bookAppointment(Long appointmentId, Long patientId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment", appointmentId));
        AppointmentValidator.validateBookAppointment(appointment);

        Patient patient = patientRepository.findById(patientId)
                        .orElseThrow(() -> new NotFoundException("Patient",patientId));
        appointment.setPatient(patient);
        return appointmentRepository.save(appointment);
    }
}
