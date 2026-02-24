package com.rummgp;

import lombok.Builder;

@Builder
public record DoctorFindCommand(
        String specialization,
        int pageNumber,
        int pageSize) {
}
