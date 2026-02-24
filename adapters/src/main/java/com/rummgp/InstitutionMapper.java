package com.rummgp;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = DoctorMapper.class)
public interface InstitutionMapper {

    @Mapping(source = "doctors", target = "doctorsId", qualifiedByName = "doctorsToId")
    InstitutionDto toDto(InstitutionEntity institution);

    @Mapping(source = "doctors", target = "doctorsId", qualifiedByName = "doctorsPojoToId")
    InstitutionDto toDto(Institution institution);

    @Mapping(target = "doctors", ignore = true)
    @Mapping(target = "id", ignore = true)
    InstitutionEntity toEntity(InstitutionCreateCommand institutionCreateCommand);

    @Mapping(target = "doctors", ignore = true)
    @Mapping(target = "id", ignore = true)
    Institution toPojo(InstitutionCreateCommand institutionCreateCommand);

    InstitutionEntity toEntity(Institution institution);

    Institution toPojo(InstitutionEntity institutionEntity);

    InstitutionFindCommand toCommand(InstitutionFindRequestDto institutionFindRequestDto);

    @Named("doctorsToId")
    default List<Long> toDoctorsId (List<DoctorEntity> doctors) {
        if (doctors == null) {
            return Collections.emptyList();
        }
        return doctors.stream()
                .map(DoctorEntity::getId)
                .toList();
    }

    @Named("doctorsPojoToId")
    default List<Long> pojoToDoctorsId (List<Doctor> doctors) {
        if (doctors == null) {
            return Collections.emptyList();
        }
        return doctors.stream()
                .map(Doctor::getId)
                .toList();
    }
}
