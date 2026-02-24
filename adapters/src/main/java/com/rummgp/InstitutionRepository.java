package com.rummgp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstitutionRepository extends JpaRepository<InstitutionEntity, Long> {

    Optional<InstitutionEntity> findByName(String name);
}
