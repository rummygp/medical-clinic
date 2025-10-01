package com.rummgp.medical_clinic.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentDto(Long id, LocalDateTime startTime, LocalDateTime endTime, Long doctorId, Long patientId){
}
