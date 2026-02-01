package com.rummgp;

import com.rummgp.Exception.AppointmentBookingException;
import com.rummgp.Exception.AppointmentExpiredException;
import com.rummgp.Exception.AppointmentOverlapException;
import com.rummgp.Exception.InvalidAppointmentTimeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppointmentValidator {

    public static void validateAppointmentCreate(AppointmentCreateCommand appointment, AppointmentRepositoryPort appointmentRepositoryPort, Long doctorId) {
        if (appointment.startTime().isBefore(LocalDateTime.now())) {
            throw new InvalidAppointmentTimeException("The appointment cannot be scheduled in the past");
        }

        if (appointment.startTime().getMinute() % 15 !=0 || appointment.endTime().getMinute() % 15 != 0) {
            throw new InvalidAppointmentTimeException("Appointment must start and end on a full quarter of an hour");
        }

        if (!appointmentRepositoryPort.findOverlapping(doctorId, appointment.startTime(), appointment.endTime()).isEmpty()) {
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
