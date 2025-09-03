package com.rummgp.medical_clinic.dto;

public record DoctorDto(Long id, String firstName, String lastName, String specialization, UserDto user){
}
