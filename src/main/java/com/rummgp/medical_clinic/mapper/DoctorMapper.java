package com.rummgp.medical_clinic.mapper;

import com.rummgp.medical_clinic.command.DoctorCreateCommand;
import com.rummgp.medical_clinic.dto.DoctorDto;
import com.rummgp.medical_clinic.model.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDto toDto(Doctor doctor);
    Doctor toEntity(DoctorCreateCommand doctorCreateCommand);
}
