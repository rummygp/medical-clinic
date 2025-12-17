package com.rummgp.medical_clinic.repository.specification;

import com.rummgp.medical_clinic.exception.NotFoundException;
import com.rummgp.medical_clinic.model.Appointment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppointmentSpecification {

    public static Specification<Appointment> hasSpecialization(String specialization) {
        if (specialization == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("doctor").get("specialization"), specialization);
    }

    public static Specification<Appointment> isAvailable(Boolean freeSlots) {
        if (freeSlots == null || !freeSlots) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
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

    public static Specification<Appointment> filter(Long doctorId, Long patientId, String specialization,
                                              LocalDateTime intervalStart, LocalDateTime intervalEnd, Boolean freeSlots) {
        Specification<Appointment> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (doctorId != null) {
            spec = spec.and((AppointmentSpecification.hasDoctor(doctorId)));
        }

        if (specialization != null && !specialization.isEmpty()) {
            spec = spec.and(AppointmentSpecification.hasSpecialization(specialization));
        }

        if (intervalStart != null && intervalEnd != null && intervalStart.isBefore(intervalEnd)) {
            spec = spec.and(AppointmentSpecification.overlapsInterval(intervalStart, intervalEnd));
        }

        if (patientId != null) {
            spec = spec.and(AppointmentSpecification.hasPatient(patientId));
        }

        if (freeSlots != null && freeSlots) {
            spec = spec.and(AppointmentSpecification.isAvailable(freeSlots));
        }

        return spec;
    }
}
