package com.rummgp.medical_clinic.dto;

import lombok.Builder;

@Builder
public record UserDto(Long id, String email, String username) {
}
