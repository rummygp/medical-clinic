package com.rummgp;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PatientCreateCommand(String firstName, String lastName, String idCardNo, String phoneNumber, LocalDate birthday, UserCreateCommand user) {
}
