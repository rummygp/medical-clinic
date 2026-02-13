package com.rummgp;

import lombok.Builder;

@Builder
public record ChangePasswordCommand(String password) {
}
