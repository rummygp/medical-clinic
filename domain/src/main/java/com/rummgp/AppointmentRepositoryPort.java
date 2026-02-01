package com.rummgp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepositoryPort {

    PagePojo<Appointment> find(AppointmentFindCommand appointmentFindCommand);

    Optional<Appointment> findById(Long id);

    Appointment save(AppointmentCreateCommand appointment, Doctor doctor);

    void delete(Appointment appointment);

    List<Appointment> findOverlapping(Long doctorId, LocalDateTime startTime, LocalDateTime endTime);

    Appointment book(Appointment appointment);
}
