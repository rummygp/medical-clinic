package com.rummgp.medical_clinic.repository;

import com.rummgp.medical_clinic.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    @Query("""
            select v from Appointment v
            where v.doctor.id = :doctorId
            and v.startTime < :endTime
            and v.endTime > :startTime
            """)
    List<Appointment> findOverlapping(Long doctorId, LocalDateTime startTime, LocalDateTime endTime);

    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);

    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);

    Page<Appointment> findByDoctorIdAndPatientId(Long doctorId, Long patientId, Pageable pageable);
}
