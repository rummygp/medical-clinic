package com.rummgp;

import lombok.Builder;

@Builder
public record UserCreateCommand(Long id, String email, String username, String password) {
}
