package com.rummgp;

import lombok.Builder;

@Builder
public record DoctorCreateCommand(String firstName, String lastName, String specialization, UserCreateCommand user) {
}
