package com.rummgp;

import lombok.Builder;

@Builder
public record UserDto(Long id, String email, String username) {
}
