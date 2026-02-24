package com.rummgp;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentCreateCommand(LocalDateTime startTime, LocalDateTime endTime, Long doctorId) {
}
