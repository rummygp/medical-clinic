package com.rummgp;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.function.Function;

@Mapper(componentModel = "spring")
public interface PageMapper {

    default <E, D> PageDto<D> toDto(PagePojo<E> page, Function<E, D> mapper) {
        return new PageDto<>(
                page.content().stream().map(mapper).toList(),
                page.page(),
                page.size(),
                page.totalElements(),
                page.totalPages()
        );
    }

    default <E, D> PagePojo<D> toPojo(Page<E> page, Function<E, D> mapper) {
        return new PagePojo<>(
                page.map(mapper).getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
