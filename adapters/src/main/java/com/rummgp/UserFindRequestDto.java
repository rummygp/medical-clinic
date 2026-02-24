package com.rummgp;

import lombok.Builder;
import org.springframework.web.bind.annotation.RequestParam;

@Builder
public record UserFindRequestDto(
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "20") int pageSize
) {
}
