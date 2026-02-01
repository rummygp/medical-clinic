package com.rummgp;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(source = "appointments", target = "appointmentsId", qualifiedByName = "appointmentsToId")
    PatientDto toDto(PatientEntity patient);

    PatientEntity toEntity(PatientCreateCommand patientCreateCommand);

    Patient toPojo(PatientEntity patientEntity);

    @Named("appointmentsToId")
    default List<Long> appointmentsToId(List<AppointmentEntity> appointments) {
        return appointments.stream()
                .map(AppointmentEntity::getId)
                .toList();
    }
}
