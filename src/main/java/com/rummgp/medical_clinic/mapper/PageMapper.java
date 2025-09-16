package com.rummgp.medical_clinic.mapper;

import com.rummgp.medical_clinic.dto.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public interface PageMapper {

    default <E, D> PageDto<D> toDto(Page<E> page, Function<E, D> mapper) {
        return new PageDto<>(
                page.map(mapper).getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
