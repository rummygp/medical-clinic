package com.rummgp;

import lombok.Builder;
import org.springframework.web.bind.annotation.RequestParam;

@Builder
public record DoctorFindRequestDto(
        @RequestParam(required = false) String specialization,
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "20") int pageSize) {
}
