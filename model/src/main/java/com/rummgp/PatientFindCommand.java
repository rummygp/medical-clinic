package com.rummgp;

import lombok.Builder;

@Builder
public record PatientFindCommand(
        int pageNumber,
        int pageSize) {
}
