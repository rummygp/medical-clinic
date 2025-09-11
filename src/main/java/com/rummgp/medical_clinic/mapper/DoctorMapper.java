package com.rummgp.medical_clinic.mapper;

import com.rummgp.medical_clinic.command.DoctorCreateCommand;
import com.rummgp.medical_clinic.dto.DoctorDto;
import com.rummgp.medical_clinic.model.Appointment;
import com.rummgp.medical_clinic.model.Doctor;
import com.rummgp.medical_clinic.model.Institution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(source = "appointments", target = "appointmentsId", qualifiedByName = "appointmentsToId")
    @Mapping(source = "institutions", target = "institutionsId", qualifiedByName = "institutionsToId")
    DoctorDto toDto(Doctor doctor);

    Doctor toEntity(DoctorCreateCommand doctorCreateCommand);

    @Named("institutionsToId")
    default List<Long> institutionsToId(List<Institution> institutions) {
        return institutions.stream()
                .map(Institution::getId)
                .toList();
    }

    @Named("appointmentsToId")
    default List<Long> appointmentsToId(List<Appointment> appointments) {
        return appointments.stream()
                .map(Appointment::getId)
                .toList();
    }
}
