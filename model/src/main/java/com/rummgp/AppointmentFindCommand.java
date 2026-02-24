package com.rummgp;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentFindCommand(
        Long doctorId,
        Long patientId,
        String specialization,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Boolean freeSlots,
        int pageNumber,
        int pageSize) {
}
