package com.rummgp.medical_clinic.mapper;

import com.rummgp.medical_clinic.dto.PatientDto;
import com.rummgp.medical_clinic.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDto toDto(Patient patient);
}
