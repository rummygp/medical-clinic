package com.rummgp.medical_clinic.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PatientDto(Long id, String firstName, String lastName, String phoneNumber, LocalDate birthday, UserDto user, List<Long> appointmentsId) {
}
