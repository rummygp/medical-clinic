package com.rummgp;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InstitutionRepositoryProvider implements InstitutionRepositoryPort {
    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;
    private final PageMapper pageMapper;

    @Override
    public Optional<Institution> findById(Long id) {
        return institutionRepository.findById(id)
                .map(institutionMapper::toPojo);
    }

    @Override
    public PagePojo<Institution> findAll(InstitutionFindCommand institutionFindCommand) {
        Pageable pageable = PageRequest.of(institutionFindCommand.pageNumber(), institutionFindCommand.pageSize());
        Page<InstitutionEntity> page = institutionRepository.findAll(pageable);
        return pageMapper.toPojo(page, institutionMapper::toPojo);
    }

    @Override
    public Institution save(Institution institution) {
        InstitutionEntity institutionEntity = institutionMapper.toEntity(institution);
        return institutionMapper.toPojo(institutionRepository.save(institutionEntity));
    }

    @Override
    public void delete(Institution institution) {
        institutionRepository.delete(institutionMapper.toEntity(institution));
    }

    @Override
    public Optional<Institution> findByName(String name) {
        return institutionRepository.findByName(name)
                .map(institutionMapper::toPojo);
    }
}
