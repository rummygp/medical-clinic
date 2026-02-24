package com.rummgp;

import java.util.Optional;

public interface InstitutionRepositoryPort {

    Optional<Institution> findById(Long id);

    Optional<Institution> findByName(String name);

    PagePojo<Institution> findAll(InstitutionFindCommand institutionFindCommand);

    Institution save(Institution institution);

    void delete(Institution institution);
}
