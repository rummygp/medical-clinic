package com.rummgp;

import lombok.Builder;

@Builder
public record InstitutionFindCommand(
        int pageNumber,
        int pageSize
) {
}
