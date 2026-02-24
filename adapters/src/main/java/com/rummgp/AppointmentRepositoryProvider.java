package com.rummgp;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AppointmentRepositoryProvider implements AppointmentRepositoryPort {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final DoctorMapper doctorMapper;
    private final PageMapper pageMapper;

    @Override
    public PagePojo<Appointment> find(AppointmentFindCommand appointmentFindCommand) {
        Specification<AppointmentEntity> spec = AppointmentSpecification.filter(appointmentFindCommand);
        Pageable pageable = PageRequest.of(appointmentFindCommand.pageNumber(), appointmentFindCommand.pageSize());
        Page<AppointmentEntity> page = appointmentRepository.findAll(spec, pageable);
        return pageMapper.toPojo(page, appointmentMapper::toPojo);
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id)
                .map(appointmentMapper::toPojo);
    }

    @Override
    public Appointment save(AppointmentCreateCommand appointment, Doctor doctor) {
        AppointmentEntity appointmentEntity = appointmentMapper.toEntity(appointment);
        DoctorEntity doctorEntity = doctorMapper.toEntity(doctor);
        appointmentEntity.setDoctor(doctorEntity);
        return appointmentMapper.toPojo(appointmentRepository.save(appointmentEntity));
    }

    @Override
    public void delete(Appointment appointment) {
        appointmentRepository.delete(appointmentMapper.toEntity(appointment));
    }

    @Override
    public List<Appointment> findOverlapping(Long doctorId, LocalDateTime startTime, LocalDateTime endTime) {
        return appointmentMapper.toPojoList(appointmentRepository.findOverlapping(doctorId, startTime, endTime));
    }

    @Override
    public Appointment book(Appointment appointment) {
        AppointmentEntity appointmentEntity = appointmentMapper.toEntity(appointment);
        return appointmentMapper.toPojo(appointmentRepository.save(appointmentEntity));
    }
}
