package com.rummgp.medical_clinic.command;

public record DoctorCreateCommand(String firstName, String lastName, String email, String specialization, UserCreateCommand user) {
}
