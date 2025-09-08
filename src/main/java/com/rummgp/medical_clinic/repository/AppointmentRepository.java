package com.rummgp.medical_clinic.repository;

import com.rummgp.medical_clinic.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
            select count(v) > 0 from Appointment v
            where v.doctor.id = :doctorId
            and v.startTime < :endTime
            and v.endTime > :startTime
            """)
    boolean existsOverlapping(Long doctorId, LocalDateTime startTime, LocalDateTime endTime);

    List<Appointment> findByPatientId(Long patientId);
}
