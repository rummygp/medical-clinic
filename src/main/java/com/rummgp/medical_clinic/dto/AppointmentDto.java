package com.rummgp.medical_clinic.dto;

import java.time.LocalDateTime;

public record AppointmentDto(Long id, LocalDateTime startTime, LocalDateTime endTime, Long doctorId, Long patientId){
}
