package com.rummgp;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DoctorSpecification {

    public static Specification<DoctorEntity> hasSpecialization(String specialization) {
        if (specialization == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("specialization"), specialization);
    }

    public static Specification<DoctorEntity> filter(DoctorFindCommand doctorFindCommand) {
        Specification<DoctorEntity> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (doctorFindCommand.specialization() != null) {
            spec = spec.and(DoctorSpecification.hasSpecialization(doctorFindCommand.specialization()));
        }

        return spec;
    }
}
