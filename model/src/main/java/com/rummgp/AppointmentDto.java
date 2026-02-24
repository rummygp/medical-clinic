package com.rummgp;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentDto(Long id, LocalDateTime startTime, LocalDateTime endTime, Long doctorId, Long patientId){
}
