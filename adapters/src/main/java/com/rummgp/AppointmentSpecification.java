package com.rummgp;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppointmentSpecification {

    public static Specification<AppointmentEntity> hasSpecialization(String specialization) {
        if (specialization == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("doctor").get("specialization"), specialization);
    }

    public static Specification<AppointmentEntity> isAvailable(Boolean freeSlots) {
        if (freeSlots == null || !freeSlots) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.isNull(root.get("patient")),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), LocalDateTime.now(ZoneId.systemDefault()))
                );
    }

    public static Specification<AppointmentEntity> hasDoctor(Long doctorId) {
        if (doctorId == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("doctor").get("id"), doctorId));
    }

    public static Specification<AppointmentEntity> hasPatient(Long patientId) {
        if (patientId == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("patient").get("id"), patientId);
    }

    public static Specification<AppointmentEntity> overlapsInterval(LocalDateTime intervalStart, LocalDateTime intervalEnd) {
        if (intervalStart == null || intervalEnd == null || !intervalStart.isBefore(intervalEnd)) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.lessThan(root.get("startTime"), intervalEnd),
                        criteriaBuilder.greaterThan(root.get("endTime"), intervalStart)
                );
    }

    public static Specification<AppointmentEntity> filter(AppointmentFindCommand appointmentFindCommand) {
        Specification<AppointmentEntity> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (appointmentFindCommand.doctorId() != null) {
            spec = spec.and((AppointmentSpecification.hasDoctor(appointmentFindCommand.doctorId())));
        }

        if (appointmentFindCommand.specialization()!= null && !appointmentFindCommand.specialization().isEmpty()) {
            spec = spec.and(AppointmentSpecification.hasSpecialization(appointmentFindCommand.specialization()));
        }

        if (appointmentFindCommand.startTime() != null && appointmentFindCommand.endTime() != null
                && appointmentFindCommand.startTime().isBefore(appointmentFindCommand.endTime())) {
            spec = spec.and(AppointmentSpecification.overlapsInterval(appointmentFindCommand.startTime(), appointmentFindCommand.endTime()));
        }

        if (appointmentFindCommand.patientId() != null) {
            spec = spec.and(AppointmentSpecification.hasPatient(appointmentFindCommand.patientId()));
        }

        if (appointmentFindCommand.freeSlots() != null && appointmentFindCommand.freeSlots()) {
            spec = spec.and(AppointmentSpecification.isAvailable(appointmentFindCommand.freeSlots()));
        }

        return spec;
    }
}
