package com.rummgp.medical_clinic.mapper;

import com.rummgp.medical_clinic.dto.PageDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.function.Function;

@Mapper(componentModel = "spring")
public interface PageMapper {

    default <E, D> PageDto<D> toDto(Page<E> page, Function<E, D> mapper) {
        page.map(mapper).getContent();

        return new PageDto<>(
                page.map(mapper).getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
