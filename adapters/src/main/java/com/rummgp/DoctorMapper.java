package com.rummgp;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = AppointmentMapper.class)
public interface DoctorMapper {

    @Mapping(source = "appointments", target = "appointmentsId", qualifiedByName = "appointmentsToId")
    @Mapping(source = "institutions", target = "institutionsId", qualifiedByName = "institutionsToId")
    DoctorDto toDto(DoctorEntity doctor);

    @Mapping(source = "appointments", target = "appointmentsId", qualifiedByName = "appointmentsToIdPojo")
    @Mapping(source = "institutions", target = "institutionsId", qualifiedByName = "institutionsToIdPojo")
    DoctorDto toDto(Doctor doctor);

    DoctorEntity toEntity(DoctorCreateCommand doctorCreateCommand);

    DoctorEntity toEntity(Doctor doctor);

    Doctor toPojo(DoctorEntity doctorEntity);

    Doctor toPojo(DoctorCreateCommand doctorCreateCommand);

    DoctorFindCommand toCommand(DoctorFindRequestDto doctorFindRequestDto);

    @Named("institutionsToId")
    default List<Long> institutionsToId(List<InstitutionEntity> institutions) {
        return institutions.stream()
                .map(InstitutionEntity::getId)
                .toList();
    }

    @Named("appointmentsToId")
    default List<Long> appointmentsToId(List<AppointmentEntity> appointments) {
        return appointments.stream()
                .map(AppointmentEntity::getId)
                .toList();
    }

    @Named("institutionsToIdPojo")
    default List<Long> institutionsToIdPojo(List<Institution> institutions) {
        return institutions.stream()
                .map(Institution::getId)
                .toList();
    }

    @Named("appointmentsToIdPojo")
    default List<Long> appointmentsToIdPojo(List<Appointment> appointments) {
        return appointments.stream()
                .map(Appointment::getId)
                .toList();
    }
}
