package com.rummgp.medical_clinic.mapper;

import com.rummgp.medical_clinic.command.InstitutionCreateCommand;
import com.rummgp.medical_clinic.dto.InstitutionDto;
import com.rummgp.medical_clinic.model.Institution;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {
    InstitutionDto toDto(Institution institution);
    Institution toEntity(InstitutionCreateCommand institutionCreateCommand);
}
