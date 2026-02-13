package com.rummgp;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(source = "appointments", target = "appointmentsId", qualifiedByName = "appointmentsToId")
    PatientDto toDto(PatientEntity patient);

    PatientEntity toEntity(PatientCreateCommand patientCreateCommand);

    Patient toPojo(PatientCreateCommand patientCreateCommand);

    Patient toPojo(PatientEntity patientEntity);

    PatientEntity toEntity(Patient patient);

    @Mapping(source = "appointments", target = "appointmentsId", qualifiedByName = "appointmentsPojoToId")
    PatientDto toDto(Patient patient);

    PatientFindCommand toCommand(PatientFindRequestDto patientFindRequestDto);

    @Named("appointmentsToId")
    default List<Long> appointmentsToId(List<AppointmentEntity> appointments) {
        if (appointments == null) {
            return Collections.emptyList();
        }
        return appointments.stream()
                .map(AppointmentEntity::getId)
                .toList();
    }

    @Named("appointmentsPojoToId")
    default List<Long> appointmentsPojoToId(List<Appointment> appointments) {
        if (appointments == null) {
            return Collections.emptyList();
        }
        return appointments.stream()
                .map(Appointment::getId)
                .toList();
    }
}
