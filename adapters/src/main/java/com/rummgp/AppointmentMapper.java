package com.rummgp;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    AppointmentEntity toEntity(AppointmentCreateCommand appointmentCreateCommand);

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "patient.id", target = "patientId")
    AppointmentDto toDto(Appointment appointment);

    @Mapping(target = "doctor", qualifiedByName = "doctorEntityForDoctor")
    Appointment toPojo(AppointmentEntity appointmentEntity);

    @Mapping(target = "doctor", qualifiedByName = "doctorForDoctorEntity")
    AppointmentEntity toEntity(Appointment appointment);

    List<Appointment> toPojoList(List<AppointmentEntity> appointmentEntities);

    AppointmentFindCommand toFindCommand(AppointmentFindRequestDto appointmentFindRequestDto);

    @Named("doctorEntityForDoctor")
    default Doctor doctorEntityToDoctor(DoctorEntity doctorEntity) {
        Optional<Doctor> doctorOptional = Optional.ofNullable(doctorEntity)
                .map(doctor -> Doctor.builder()
                        .id(doctor.getId())
                        .firstName(doctor.getFirstName())
                        .lastName(doctor.getLastName())
                        .specialization(doctor.getSpecialization())
                        .user(userEntityToUserPojo(doctor.getUser()))
                        .institutions(doctor.getInstitutions().stream()
                                .map(institution -> Institution.builder()
                                        .id(institution.getId())
                                        .build())
                                .toList())
                        .build()
                );
        List<Appointment> appointments = new ArrayList<>();
        doctorOptional.ifPresent(doctor -> {
            Optional.ofNullable(doctorEntity.getAppointments()).orElse(new ArrayList<>()).stream()
                    .map(appointment -> Appointment.builder()
                            .id(appointment.getId())
                            .startTime(appointment.getStartTime())
                            .endTime(appointment.getEndTime())
                            .doctor(doctor)
                            .build())
                    .forEach(appointments::add);
        });
        doctorOptional.ifPresent(doctor -> doctor.setAppointments(appointments));
        return doctorOptional.orElse(null);
    };

    default User userEntityToUserPojo(UserEntity user) {
        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    @Named("doctorForDoctorEntity")
    default DoctorEntity doctorToDoctorEntity(Doctor doctor) {
        Optional<DoctorEntity> doctorEntityOptional = Optional.ofNullable(doctor)
                .map(doctor1 -> DoctorEntity.builder()
                        .id(doctor1.getId())
                        .firstName(doctor1.getFirstName())
                        .lastName(doctor1.getLastName())
                        .specialization(doctor1.getSpecialization())
                        .user(userPojoToUserEntity(doctor1.getUser()))
                        .institutions(doctor1.getInstitutions().stream()
                                .map(institution -> InstitutionEntity.builder()
                                        .id(institution.getId())
                                        .build())
                                .toList())
                        .build());
        List<AppointmentEntity> appointments = new ArrayList<>();
        doctorEntityOptional.ifPresent(doctorEntity -> {
            Optional.ofNullable(doctor.getAppointments()).orElse(new ArrayList<>()).stream()
                    .map(appointment -> AppointmentEntity.builder()
                            .id(appointment.getId())
                            .startTime(appointment.getStartTime())
                            .endTime(appointment.getEndTime())
                            .doctor(doctorEntity)
                            .build())
                    .forEach(appointments::add);
        });
        doctorEntityOptional.ifPresent(doctorEntity -> doctorEntity.setAppointments(appointments));
        return doctorEntityOptional.orElse(null);
    }

    default UserEntity userPojoToUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
