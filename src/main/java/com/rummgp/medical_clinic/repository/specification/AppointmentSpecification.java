package com.rummgp.medical_clinic.repository.specification;

import com.rummgp.medical_clinic.model.Appointment;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class AppointmentSpecification {

    public static Specification<Appointment> hasSpecialization(String specialization) {
        if (specialization == null || specialization.isBlank()) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("doctor").get("specialization"), specialization);
    }

    public static Specification<Appointment> isOnDate(LocalDate date) {
        if (date == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDayExclusive = date.plusDays(1).atStartOfDay();

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.lessThan(root.get("startTime"), endOfDayExclusive),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), startOfDay)
                );
    }

    public static Specification<Appointment> isAvailable() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("patient"));
    }

    public static Specification<Appointment> hasDoctor(Long doctorId) {
        if (doctorId == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("doctor").get("id"), doctorId));
    }
}
