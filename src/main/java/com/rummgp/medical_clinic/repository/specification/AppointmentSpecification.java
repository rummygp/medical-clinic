package com.rummgp.medical_clinic.repository.specification;

import com.rummgp.medical_clinic.model.Appointment;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class AppointmentSpecification {

    public static Specification<Appointment> hasSpecialization(String specialization) {
        if (specialization == null) {
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
                criteriaBuilder.and(
                        criteriaBuilder.isNull(root.get("patient")),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), LocalDateTime.now(ZoneId.systemDefault()))
                );
    }

    public static Specification<Appointment> hasDoctor(Long doctorId) {
        if (doctorId == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("doctor").get("id"), doctorId));
    }

    public static Specification<Appointment> hasPatient(Long patientId) {
        if (patientId == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("patient").get("id"), patientId);
    }

    public static Specification<Appointment> overlapsInterval(LocalDateTime intervalStart, LocalDateTime intervalEnd) {
        if (intervalStart == null || intervalEnd == null || !intervalStart.isBefore(intervalEnd)) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.lessThan(root.get("startTime"), intervalEnd),
                        criteriaBuilder.greaterThan(root.get("endTime"), intervalStart)
                );
    }
}
