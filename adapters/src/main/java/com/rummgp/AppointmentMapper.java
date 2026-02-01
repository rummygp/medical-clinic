package com.rummgp;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    AppointmentEntity toEntity(AppointmentCreateCommand appointmentCreateCommand);

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")
    AppointmentDto toDto(Appointment appointment);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Appointment toPojo(AppointmentEntity appointmentEntity);

    AppointmentEntity toEntity(Appointment appointment);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    List<Appointment> toPojoList(List<AppointmentEntity> appointmentEntities);

    AppointmentFindCommand toFindCommand(AppointmentFindRequestDto appointmentFindRequestDto);
}
