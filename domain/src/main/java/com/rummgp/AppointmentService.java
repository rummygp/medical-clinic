package com.rummgp;

import com.rummgp.Exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepositoryPort appointmentRepositoryPort;
    private final DoctorRepositoryPort doctorRepositoryPort;
    private final PatientRepositoryPort patientRepositoryPort;

    public PagePojo<Appointment> find(AppointmentFindCommand appointmentFindCommand) {
        if (appointmentFindCommand.doctorId() != null) {
            doctorRepositoryPort.findById(appointmentFindCommand.doctorId())
                    .orElseThrow(() -> new NotFoundException("Doctor", appointmentFindCommand.doctorId()));
        }
        if (appointmentFindCommand.patientId() != null) {
            patientRepositoryPort.findById(appointmentFindCommand.patientId())
                    .orElseThrow(() -> new NotFoundException("Patient", appointmentFindCommand.patientId()));
        }
        return appointmentRepositoryPort.find(appointmentFindCommand);
    }

    public Appointment add(AppointmentCreateCommand appointment) {
        Doctor doctor = doctorRepositoryPort.findById(appointment.doctorId())
                .orElseThrow(() -> new NotFoundException("Doctor", appointment.doctorId()));
        AppointmentValidator.validateAppointmentCreate(appointment, appointmentRepositoryPort, doctor.getId());
        return appointmentRepositoryPort.save(appointment, doctor);
    }

    public Appointment bookAppointment(Long appointmentId, Long patientId) {
        Appointment appointment = appointmentRepositoryPort.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment", appointmentId));
        AppointmentValidator.validateBookAppointment(appointment);
        Patient patient = patientRepositoryPort.findById(patientId)
                .orElseThrow(() -> new NotFoundException("Patient", patientId));
        appointment.setPatient(patient);
        return appointmentRepositoryPort.book(appointment);
    }

    public void delete(Long id) {
        Appointment appointment = appointmentRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment", id));
        appointmentRepositoryPort.delete(appointment);
    }
}
