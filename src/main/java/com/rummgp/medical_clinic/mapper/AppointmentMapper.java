package com.rummgp.medical_clinic.mapper;

import com.rummgp.medical_clinic.command.AppointmentCreateCommand;
import com.rummgp.medical_clinic.dto.AppointmentDto;
import com.rummgp.medical_clinic.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Appointment toEntity(AppointmentCreateCommand appointmentCreateCommand);

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")
    AppointmentDto toDto(Appointment appointment);
}
