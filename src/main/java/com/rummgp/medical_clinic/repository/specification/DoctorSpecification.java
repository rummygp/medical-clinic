package com.rummgp.medical_clinic.repository.specification;

import com.rummgp.medical_clinic.model.Doctor;
import org.springframework.data.jpa.domain.Specification;

public final class DoctorSpecification {

    public static Specification<Doctor> hasSpecialization(String specialization) {
        if (specialization == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("specialization"), specialization);
    }
}
