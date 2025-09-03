package com.rummgp.medical_clinic.dto;

import java.time.LocalDate;

public record PatientDto(Long id, String firstName, String lastName, String phoneNumber, LocalDate birthday, UserDto user) {
}
