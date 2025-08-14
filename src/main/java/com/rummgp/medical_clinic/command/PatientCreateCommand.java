package com.rummgp.medical_clinic.command;

import java.time.LocalDate;

public record PatientCreateCommand(String firstName, String lastName, String email, String password, String phoneNumber, LocalDate birthday) {
}
