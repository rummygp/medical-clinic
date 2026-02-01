package com.rummgp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InstitutionRepositoryProvider implements InstitutionRepositoryPort {

    @Override
    public Optional<Institution> findById(Long id) {
        return Optional.empty();
    }
}
