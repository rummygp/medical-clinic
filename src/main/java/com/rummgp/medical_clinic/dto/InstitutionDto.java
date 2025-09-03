package com.rummgp.medical_clinic.dto;

import java.util.List;

public record InstitutionDto(Long id, String name, String city, String postalCode, String street, String buildingNo, List<Long> doctorsId) {
}
