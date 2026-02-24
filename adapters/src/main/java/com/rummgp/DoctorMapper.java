package com.rummgp;

import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Mapping(source = "institutions", target = "institutions", qualifiedByName = "noLoopForInstitutions")
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

    @Named("noLoopForInstitutions")
    default List<Institution> institutionMapper(List<InstitutionEntity> institutionEntities) {
       List<Institution> institutions = new ArrayList<>();
       Optional.ofNullable(institutionEntities).orElse(new ArrayList<>()).stream()
               .map(institution -> Institution.builder()
                       .id(institution.getId())
                       .city(institution.getCity())
                       .name(institution.getName())
                       .street(institution.getStreet())
                       .postalCode(institution.getPostalCode())
                       .buildingNo(institution.getBuildingNo())
                       .doctors(institution.getDoctors().stream()
                               .map(doctor -> Doctor.builder()
                                       .id(doctor.getId())
                                       .build()).toList())
                       .build())
               .forEach(institutions::add);
       return institutions;
    }
}
