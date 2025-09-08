package com.rummgp.medical_clinic.command;

import java.time.LocalDateTime;

public record AppointmentCreateCommand(LocalDateTime startTime, LocalDateTime endTime) {
}
