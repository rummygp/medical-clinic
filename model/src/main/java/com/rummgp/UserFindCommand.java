package com.rummgp;

import lombok.Builder;

@Builder
public record UserFindCommand(int pageNumber,
                              int pageSize) {
}
