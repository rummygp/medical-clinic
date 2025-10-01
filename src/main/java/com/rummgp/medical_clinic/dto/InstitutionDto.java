package com.rummgp.medical_clinic.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record InstitutionDto(Long id, String name, String city, String postalCode, String street, String buildingNo, List<Long> doctorsId) {
}
