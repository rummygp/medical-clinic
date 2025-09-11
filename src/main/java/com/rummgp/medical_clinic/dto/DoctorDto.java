package com.rummgp.medical_clinic.dto;

import java.util.List;

public record DoctorDto(Long id, String firstName, String lastName, String specialization, UserDto user, List<Long> institutionsId, List<Long> appointmentsId) {
}
