package com.rummgp.medical_clinic.mapper;

import com.rummgp.medical_clinic.command.PatientCreateCommand;
import com.rummgp.medical_clinic.dto.PatientDto;
import com.rummgp.medical_clinic.model.Appointment;
import com.rummgp.medical_clinic.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(source = "appointments", target = "appointmentsId", qualifiedByName = "appointmentsToId")
    PatientDto toDto(Patient patient);

    Patient toEntity(PatientCreateCommand patientCreateCommand);

    @Named("appointmentsToId")
    default List<Long> appointmentsToId(List<Appointment> appointments) {
        return appointments.stream()
                .map(Appointment::getId)
                .toList();
    }
}
