package com.rummgp.medical_clinic.validator;

import com.rummgp.medical_clinic.exception.AppointmentBookingException;
import com.rummgp.medical_clinic.exception.AppointmentExpiredException;
import com.rummgp.medical_clinic.exception.AppointmentOverlapException;
import com.rummgp.medical_clinic.exception.InvalidAppointmentTimeException;
import com.rummgp.medical_clinic.model.Appointment;
import com.rummgp.medical_clinic.repository.AppointmentRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppointmentValidator {

    public static void validateAppointmentCreate(Appointment appointment, AppointmentRepository appointmentRepository, Long doctorId) {
        if (appointment.getStartTime().isBefore(LocalDateTime.now())) {
            throw new InvalidAppointmentTimeException("The appointment cannot be scheduled in the past");
        }

        if (appointment.getStartTime().getMinute() % 15 !=0 || appointment.getEndTime().getMinute() % 15 != 0) {
            throw new InvalidAppointmentTimeException("Appointment must start and end on a full quarter of an hour");
        }

        if (appointmentRepository.existsOverlapping(doctorId, appointment.getStartTime(), appointment.getEndTime())) {
            throw new AppointmentOverlapException("The appointment overlaps with another appointment of this doctor");
        }
    }

    public static void validateBookAppointment(Appointment appointment) {
        if (appointment.getPatient() != null) {
            throw new AppointmentBookingException("The appointment is already booked");
        }
        if (appointment.getEndTime().isBefore(LocalDateTime.now())) {
            throw new AppointmentExpiredException("Cannot sign up to appointment in the past");
        }
    }
}
