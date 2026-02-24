package com.rummgp;

import lombok.Builder;

import java.util.List;

public record PagePojo<T>(List<T> content, int page, int size, long totalElements, int totalPages) {
}
