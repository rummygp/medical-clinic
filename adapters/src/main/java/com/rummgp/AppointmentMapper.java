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
                        .user(User.builder()
                                .id(doctor.getUser().getId())
                                .username(doctor.getUser().getUsername())
                                .email(doctor.getUser().getEmail())
                                .password(doctor.getUser().getPassword())
                                .build())
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
    }

    @Named("doctorForDoctorEntity")
    default DoctorEntity doctorToDoctorEntity(Doctor doctor) {
        Optional<DoctorEntity> doctorEntityOptional = Optional.ofNullable(doctor)
                .map(doctor1 -> DoctorEntity.builder()
                        .id(doctor1.getId())
                        .firstName(doctor1.getFirstName())
                        .lastName(doctor1.getLastName())
                        .specialization(doctor1.getSpecialization())
                        .user(UserEntity.builder()
                                .id(doctor1.getUser().getId())
                                .email(doctor1.getUser().getEmail())
                                .username(doctor1.getUser().getUsername())
                                .password(doctor1.getUser().getPassword())
                                .build())
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

    default Patient patientEntityToPatient(PatientEntity patientEntity) {
        Optional<Patient> patientOptional = Optional.ofNullable(patientEntity)
                .map(patient -> Patient.builder()
                        .id(patient.getId())
                        .firstName(patient.getFirstName())
                        .lastName(patient.getLastName())
                        .birthday(patient.getBirthday())
                        .idCardNo(patient.getIdCardNo())
                        .phoneNumber(patient.getPhoneNumber())
                        .user(User.builder()
                                .id(patient.getUser().getId())
                                .username(patient.getUser().getUsername())
                                .email(patient.getUser().getEmail())
                                .password(patient.getUser().getPassword())
                                .build())
                        .build());
        List<Appointment> appointments = new ArrayList<>();
        patientOptional.ifPresent(patient -> {
            Optional.ofNullable(patientEntity.getAppointments()).orElse(new ArrayList<>()).stream()
                    .map(appointment -> Appointment.builder()
                            .id(appointment.getId())
                            .startTime(appointment.getStartTime())
                            .endTime(appointment.getEndTime())
                            .patient(patient)
                            .doctor(doctorEntityToDoctor(appointment.getDoctor()))
                            .build())
                    .forEach(appointments::add);
        });
        patientOptional.ifPresent(patient -> patient.setAppointments(appointments));
        return patientOptional.orElse(null);
    }

    default PatientEntity patientToPatientEntity(Patient patient) {
        Optional<PatientEntity> patientEntityOptional = Optional.ofNullable(patient)
                .map(patient1 -> PatientEntity.builder()
                        .id(patient1.getId())
                        .firstName(patient1.getFirstName())
                        .lastName(patient1.getLastName())
                        .phoneNumber(patient1.getPhoneNumber())
                        .birthday(patient1.getBirthday())
                        .user(UserEntity.builder()
                                .id(patient1.getUser().getId())
                                .email(patient1.getUser().getEmail())
                                .username(patient1.getUser().getUsername())
                                .password(patient1.getUser().getPassword())
                                .build())
                        .idCardNo(patient1.getIdCardNo())
                        .build());
        List<AppointmentEntity> appointments = new ArrayList<>();
        patientEntityOptional.ifPresent(patientEntity -> {
            Optional.ofNullable(patient.getAppointments()).orElse(new ArrayList<>()).stream()
                    .map(appointment -> AppointmentEntity.builder()
                            .id(appointment.getId())
                            .startTime(appointment.getStartTime())
                            .endTime(appointment.getEndTime())
                            .doctor(doctorToDoctorEntity(appointment.getDoctor()))
                            .patient(patientEntity)
                            .build())
                    .forEach(appointments::add);
        });
        patientEntityOptional.ifPresent(patientEntity -> patientEntity.setAppointments(appointments));
        return patientEntityOptional.orElse(null);
    }
}
