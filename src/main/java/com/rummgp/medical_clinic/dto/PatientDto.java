package com.rummgp.medical_clinic.dto;

import java.time.LocalDate;

public record PatientDto(String firstName, String lastName, String email, String phoneNumber, LocalDate birthday) {
}
