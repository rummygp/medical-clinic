package com.rummgp.medical_clinic.command;

import java.time.LocalDate;

public record PatientCreateCommand(String firstName, String lastName, String idCardNo, String phoneNumber, LocalDate birthday, UserCreateCommand user) {
}
