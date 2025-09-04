package com.rummgp.medical_clinic.mapper;

import com.rummgp.medical_clinic.command.InstitutionCreateCommand;
import com.rummgp.medical_clinic.dto.InstitutionDto;
import com.rummgp.medical_clinic.model.Doctor;
import com.rummgp.medical_clinic.model.Institution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {

    @Mapping(source = "doctors", target = "doctorsId", qualifiedByName = "doctorsToId")
    InstitutionDto toDto(Institution institution);

    Institution toEntity(InstitutionCreateCommand institutionCreateCommand);

    @Named("doctorsToId")
    default List<Long> toDoctorsId (List<Doctor> doctors) {
        return doctors.stream()
                .map(Doctor::getId)
                .toList();
    }
}
