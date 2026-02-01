package com.rummgp;

import java.util.Optional;

public interface InstitutionRepositoryPort {

    Optional<Institution> findById(Long id);
}
