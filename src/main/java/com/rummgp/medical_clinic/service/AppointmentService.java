package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.command.AppointmentCreateCommand;
import com.rummgp.medical_clinic.exception.NotFoundException;
import com.rummgp.medical_clinic.mapper.AppointmentMapper;
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
    private final AppointmentMapper appointmentMapper;

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
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
                        .orElseThrow(() -> new NotFoundException("Patient",patientId));
        appointment.setPatient(patient);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findAllAppointments(Long id) {
        patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient",id));
        return appointmentRepository.findByPatientId(id);
    }
}
