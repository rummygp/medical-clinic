package com.rummgp.medical_clinic.repository.specification;

import com.rummgp.medical_clinic.model.Doctor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DoctorSpecification {

    public static Specification<Doctor> hasSpecialization(String specialization) {
        if (specialization == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("specialization"), specialization);
    }

    public static Specification<Doctor> filter(String specialization) {
        Specification<Doctor> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (specialization != null) {
            spec = spec.and(DoctorSpecification.hasSpecialization(specialization));
        }

        return spec;
    }
}
