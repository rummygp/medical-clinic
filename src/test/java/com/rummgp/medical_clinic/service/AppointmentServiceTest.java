package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.command.AppointmentCreateCommand;
import com.rummgp.medical_clinic.dto.AppointmentDto;
import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.exception.*;
import com.rummgp.medical_clinic.mapper.AppointmentMapper;
import com.rummgp.medical_clinic.mapper.PageMapper;
import com.rummgp.medical_clinic.model.Appointment;
import com.rummgp.medical_clinic.model.Doctor;
import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.repository.AppointmentRepository;
import com.rummgp.medical_clinic.repository.DoctorRepository;
import com.rummgp.medical_clinic.repository.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {
    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private AppointmentMapper appointmentMapper;
    private PageMapper pageMapper;
    private AppointmentService appointmentService;

    @BeforeEach
    void setup() {
        this.appointmentRepository = Mockito.mock(AppointmentRepository.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.appointmentMapper = Mappers.getMapper(AppointmentMapper.class);
        this.pageMapper = Mappers.getMapper(PageMapper.class);
        this.appointmentService = new AppointmentService(appointmentRepository, doctorRepository, patientRepository, appointmentMapper, pageMapper);
    }

    //todo
//    @Test
//    void find_ByDoctorAndPatientId_AppointmentReturned() {
//        //given
//        Doctor doctor = Doctor.builder().id(1L).build();
//        Patient patient = Patient.builder().id(2L).build();
//        Appointment appointment1 = Appointment.builder()
//                .id(3L)
//                .startTime(LocalDateTime.of(3025, 9, 29, 10, 0))
//                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
//                .doctor(doctor)
//                .patient(patient)
//                .build();
//        Appointment appointment2 = Appointment.builder()
//                .id(4L)
//                .startTime(LocalDateTime.of(3025, 10, 29, 11, 0))
//                .endTime(LocalDateTime.of(3025, 10, 29, 11, 30))
//                .doctor(doctor)
//                .patient(patient)
//                .build();
//        List<Appointment> appointments = List.of(appointment1, appointment2);
//        Pageable pageable = PageRequest.of(0, 2);
//        Page<Appointment> page = new PageImpl<>(appointments, pageable, appointments.size());
//
//        when(appointmentRepository.findByDoctorIdAndPatientId(1L, 2L, pageable)).thenReturn(page);
//        //when
//        PageDto<AppointmentDto> result = appointmentService.find(1L, 2L, pageable);
//        //then
//        Assertions.assertAll(
//                () -> assertEquals(3L, result.content().get(0).id()),
//                () -> assertEquals(LocalDateTime.of(3025, 9, 29, 10, 0), result.content().get(0).startTime()),
//                () -> assertEquals(LocalDateTime.of(3025, 9, 29, 10, 30), result.content().get(0).endTime()),
//                () -> assertEquals(1L, result.content().get(0).doctorId()),
//                () -> assertEquals(2L, result.content().get(0).patientId()),
//                () -> assertEquals(4L, result.content().get(1).id()),
//                () -> assertEquals(LocalDateTime.of(3025, 10, 29, 11, 0), result.content().get(1).startTime()),
//                () -> assertEquals(LocalDateTime.of(3025, 10, 29, 11, 30), result.content().get(1).endTime()),
//                () -> assertEquals(1L, result.content().get(1).doctorId()),
//                () -> assertEquals(2L, result.content().get(1).patientId())
//        );
//
//        verify(appointmentRepository).findByDoctorIdAndPatientId(1L, 2L, pageable);
//        verifyNoMoreInteractions(appointmentRepository);
//    }
//
//    @Test
//    void find_ByDoctorId_AppointmentsReturned() {
//        Doctor doctor = Doctor.builder().id(1L).build();
//        Appointment appointment1 = Appointment.builder()
//                .id(2L)
//                .startTime(LocalDateTime.of(3025, 9, 29, 10, 0))
//                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
//                .doctor(doctor)
//                .patient(null)
//                .build();
//        Appointment appointment2 = Appointment.builder()
//                .id(3L)
//                .startTime(LocalDateTime.of(3025, 10, 29, 11, 0))
//                .endTime(LocalDateTime.of(3025, 10, 29, 11, 30))
//                .doctor(doctor)
//                .patient(null)
//                .build();
//
//        List<Appointment> appointments = List.of(appointment1, appointment2);
//        Pageable pageable = PageRequest.of(0, 2);
//        Page<Appointment> page = new PageImpl<>(appointments, pageable, appointments.size());
//
//        when(appointmentRepository.findByDoctorId(1L, pageable)).thenReturn(page);
//        //when
//        PageDto<AppointmentDto> result = appointmentService.find(1L, null, pageable);
//        //then
//        Assertions.assertAll(
//                () -> assertEquals(2L, result.content().get(0).id()),
//                () -> assertEquals(LocalDateTime.of(3025, 9, 29, 10, 0), result.content().get(0).startTime()),
//                () -> assertEquals(LocalDateTime.of(3025, 9, 29, 10, 30), result.content().get(0).endTime()),
//                () -> assertEquals(1L, result.content().get(0).doctorId()),
//                () -> assertNull(result.content().get(0).patientId()),
//                () -> assertEquals(3L, result.content().get(1).id()),
//                () -> assertEquals(LocalDateTime.of(3025, 10, 29, 11, 0), result.content().get(1).startTime()),
//                () -> assertEquals(LocalDateTime.of(3025, 10, 29, 11, 30), result.content().get(1).endTime()),
//                () -> assertEquals(1L, result.content().get(1).doctorId()),
//                () -> assertNull(result.content().get(1).patientId())
//        );
//
//        verify(appointmentRepository).findByDoctorId(1L, pageable);
//        verifyNoMoreInteractions(appointmentRepository);
//    }
//
//    @Test
//    void find_ByPatientId_AppointmentsReturned() {
//        Patient patient = Patient.builder().id(1L).build();
//        Appointment appointment1 = Appointment.builder()
//                .id(2L)
//                .startTime(LocalDateTime.of(3025, 9, 29, 10, 0))
//                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
//                .doctor(null)
//                .patient(patient)
//                .build();
//        Appointment appointment2 = Appointment.builder()
//                .id(3L)
//                .startTime(LocalDateTime.of(3025, 10, 29, 11, 0))
//                .endTime(LocalDateTime.of(3025, 10, 29, 11, 30))
//                .doctor(null)
//                .patient(patient)
//                .build();
//        List<Appointment> appointments = List.of(appointment1, appointment2);
//        Pageable pageable = PageRequest.of(0, 2);
//        Page<Appointment> page = new PageImpl<>(appointments, pageable, appointments.size());
//
//        when(appointmentRepository.findByPatientId(1L, pageable)).thenReturn(page);
//        //when
//        PageDto<AppointmentDto> result = appointmentService.find(null, 1L, pageable);
//        //then
//        Assertions.assertAll(
//                () -> assertEquals(2L, result.content().get(0).id()),
//                () -> assertEquals(LocalDateTime.of(3025, 9, 29, 10, 0), result.content().get(0).startTime()),
//                () -> assertEquals(LocalDateTime.of(3025, 9, 29, 10, 30), result.content().get(0).endTime()),
//                () -> assertEquals(1L, result.content().get(0).patientId()),
//                () -> assertNull(result.content().get(0).doctorId()),
//                () -> assertEquals(3L, result.content().get(1).id()),
//                () -> assertEquals(LocalDateTime.of(3025, 10, 29, 11, 0), result.content().get(1).startTime()),
//                () -> assertEquals(LocalDateTime.of(3025, 10, 29, 11, 30), result.content().get(1).endTime()),
//                () -> assertEquals(1L, result.content().get(1).patientId()),
//                () -> assertNull(result.content().get(1).doctorId())
//        );
//
//        verify(appointmentRepository).findByPatientId(1L, pageable);
//        verifyNoMoreInteractions(appointmentRepository);
//    }
//
//    @Test
//    void find_findAll_AppointmentsReturned() {
//        Doctor doctor = Doctor.builder().id(1L).build();
//        Appointment appointment1 = Appointment.builder()
//                .id(2L)
//                .startTime(LocalDateTime.of(3025, 9, 29, 10, 0))
//                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
//                .doctor(doctor)
//                .patient(null)
//                .build();
//        Appointment appointment2 = Appointment.builder()
//                .id(3L)
//                .startTime(LocalDateTime.of(3025, 10, 29, 11, 0))
//                .endTime(LocalDateTime.of(3025, 10, 29, 11, 30))
//                .doctor(doctor)
//                .patient(null)
//                .build();
//        List<Appointment> appointments = List.of(appointment1, appointment2);
//        Pageable pageable = PageRequest.of(0, 2);
//        Page<Appointment> page = new PageImpl<>(appointments, pageable, appointments.size());
//
//        when(appointmentRepository.findAll(pageable)).thenReturn(page);
//        //when
//        PageDto<AppointmentDto> result = appointmentService.find(null, null, pageable);
//        //then
//        Assertions.assertAll(
//                () -> assertEquals(2L, result.content().get(0).id()),
//                () -> assertEquals(LocalDateTime.of(3025, 9, 29, 10, 0), result.content().get(0).startTime()),
//                () -> assertEquals(LocalDateTime.of(3025, 9, 29, 10, 30), result.content().get(0).endTime()),
//                () -> assertEquals(1L, result.content().get(0).doctorId()),
//                () -> assertNull(result.content().get(0).patientId()),
//                () -> assertEquals(3L, result.content().get(1).id()),
//                () -> assertEquals(LocalDateTime.of(3025, 10, 29, 11, 0), result.content().get(1).startTime()),
//                () -> assertEquals(LocalDateTime.of(3025, 10, 29, 11, 30), result.content().get(1).endTime()),
//                () -> assertEquals(1L, result.content().get(1).doctorId()),
//                () -> assertNull(result.content().get(1).patientId())
//        );
//
//        verify(appointmentRepository).findAll(pageable);
//        verifyNoMoreInteractions(appointmentRepository);
//    }

    @Test
    void add_DataCorrect_AppointmentReturned() {
        //given
        Doctor doctor = Doctor.builder().id(1L).firstName("Adam").build();
        AppointmentCreateCommand appointmentCreateCommand = AppointmentCreateCommand.builder()
                .startTime(LocalDateTime.of(3025, 9, 29, 10, 0))
                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
                .doctorId(1L)
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.save(any(Appointment.class))).then(returnsFirstArg());
        //when
        Appointment result = appointmentService.add(appointmentCreateCommand);
        //then
        Assertions.assertAll(
                () -> assertEquals(LocalDateTime.of(3025, 9, 29, 10, 0), result.getStartTime()),
                () -> assertEquals(LocalDateTime.of(3025, 9, 29, 10, 30), result.getEndTime()),
                () -> assertEquals(1L, result.getDoctor().getId()),
                () -> assertEquals("Adam", result.getDoctor().getFirstName()),
                () -> assertNull(result.getPatient())
        );

        verify(doctorRepository).findById(1L);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void add_DoctorNotFound_ExceptionThrown() {
        //given
        Long doctorId = 1L;
        AppointmentCreateCommand appointmentCreateCommand = AppointmentCreateCommand.builder()
                .startTime(LocalDateTime.of(3025, 9, 29, 10, 0))
                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
                .doctorId(1L)
                .build();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> appointmentService.add(appointmentCreateCommand));
        //then
        Assertions.assertAll(
                () -> assertEquals("Doctor with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );
    }

    @Test
    void add_ScheduledAppointmentInPast_ExceptionReturned() {
        //given
        Doctor doctor = Doctor.builder().id(1L).build();
        AppointmentCreateCommand appointmentCreateCommand = AppointmentCreateCommand.builder()
                .startTime(LocalDateTime.of(2000, 9, 28, 10, 0))
                .endTime(LocalDateTime.of(2000, 9, 29, 10, 30))
                .doctorId(1L)
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        //when
        InvalidAppointmentTimeException exception = Assertions.assertThrowsExactly(InvalidAppointmentTimeException.class,
                () -> appointmentService.add(appointmentCreateCommand));
        //then
        Assertions.assertAll(
                () -> assertEquals("The appointment cannot be scheduled in the past", exception.getMessage()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus())
        );

        verify(doctorRepository).findById(1L);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void add_InvalidTimeStamp_ExceptionThrown() {
        //given
        Doctor doctor = Doctor.builder().id(1L).build();
        AppointmentCreateCommand appointmentCreateCommand = AppointmentCreateCommand.builder()
                .startTime(LocalDateTime.of(3025, 9, 29, 10, 1))
                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
                .doctorId(1L)
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        //when
        InvalidAppointmentTimeException exception = Assertions.assertThrowsExactly(InvalidAppointmentTimeException.class,
                () -> appointmentService.add(appointmentCreateCommand));
        //then
        Assertions.assertAll(
                () -> assertEquals("Appointment must start and end on a full quarter of an hour", exception.getMessage()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus())
        );

        verify(doctorRepository).findById(1L);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void add_AppointmentOverlap_ExceptionThrown() {
        //given
        Doctor doctor = Doctor.builder().id(1L).build();
        AppointmentCreateCommand appointmentCreateCommand = AppointmentCreateCommand.builder()
                .startTime(LocalDateTime.of(3025, 9, 29, 10, 0))
                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
                .doctorId(1L)
                .build();
        Appointment appointment = Appointment.builder()
                .id(2L)
                .startTime(LocalDateTime.of(3025, 9, 29, 10, 0))
                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
                .doctor(doctor)
                .patient(null)
                .build();
        List<Appointment> appointments = List.of(appointment);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findOverlapping(1L, LocalDateTime.of(3025, 9, 29, 10, 0), LocalDateTime.of(3025, 9, 29, 10, 30)))
                .thenReturn(appointments);
        //when
        AppointmentOverlapException exception = Assertions.assertThrowsExactly(AppointmentOverlapException.class,
                () -> appointmentService.add(appointmentCreateCommand));
        //then
        Assertions.assertAll(
                () -> assertEquals("The appointment overlaps with another appointment of this doctor", exception.getMessage()),
                () -> assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus())
        );

        verify(doctorRepository).findById(1L);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void bookAppointment_DataCorrect_AppointmentReturned() {
        //given
        Doctor doctor = Doctor.builder().id(1L).build();
        Patient patient = Patient.builder().id(2L).build();
        Appointment appointment = Appointment.builder()
                .id(3L)
                .startTime(LocalDateTime.of(3025, 9, 29, 10, 0))
                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
                .doctor(doctor)
                .patient(null)
                .build();

        when(appointmentRepository.findById(3L)).thenReturn(Optional.of(appointment));
        when(patientRepository.findById(2L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        //when
        Appointment result = appointmentService.bookAppointment(3L, 2L);
        //then
        Assertions.assertAll(
                () -> assertEquals(3L, result.getId()),
                () -> assertEquals(LocalDateTime.of(3025, 9, 29, 10, 0), result.getStartTime()),
                () -> assertEquals(LocalDateTime.of(3025, 9, 29, 10, 30), result.getEndTime()),
                () -> assertEquals(1L, result.getDoctor().getId()),
                () -> assertEquals(2L, result.getPatient().getId())
        );

        verify(appointmentRepository).findById(3L);
        verify(patientRepository).findById(2L);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void bookAppointment_AppointmentNotFound_ExceptionThrown() {
        //given
        Long appointmentId = 1L;
        Long patientId = 2L;

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> appointmentService.bookAppointment(appointmentId, patientId));
        //then
        Assertions.assertAll(
                () -> assertEquals("Appointment with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );

        verify(appointmentRepository).findById(appointmentId);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void bookAppointment_PatientNotFound_ExceptionThrown() {
        //given
        Doctor doctor = Doctor.builder().id(1L).build();
        Long patientId = 2L;
        Appointment appointment = Appointment.builder()
                .id(3L)
                .startTime(LocalDateTime.of(3025, 9, 29, 10, 0))
                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
                .doctor(doctor)
                .patient(null)
                .build();

        when(appointmentRepository.findById(3L)).thenReturn(Optional.of(appointment));
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> appointmentService.bookAppointment(3L, patientId));
        //then
        Assertions.assertAll(
                () -> assertEquals("Patient with id: 2 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );

        verify(appointmentRepository).findById(3L);
        verify(patientRepository).findById(2L);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void bookAppointment_AppointmentAlreadyBooked_ExceptionThrown() {
        //given
        Doctor doctor = Doctor.builder().id(1L).build();
        Patient patient1 = Patient.builder().id(2L).build();
        Appointment appointment = Appointment.builder()
                .id(4L)
                .startTime(LocalDateTime.of(3025, 9, 29, 10, 0))
                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
                .doctor(doctor)
                .patient(patient1)
                .build();

        when(appointmentRepository.findById(4L)).thenReturn(Optional.of(appointment));
        //when
        AppointmentBookingException exception = Assertions.assertThrowsExactly(AppointmentBookingException.class,
                () -> appointmentService.bookAppointment(4L, 3L));
        //then
        Assertions.assertAll(
                () -> assertEquals("The appointment is already booked", exception.getMessage()),
                () -> assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus())
        );

        verify(appointmentRepository).findById(4L);
        verify(patientRepository, never()).findById(anyLong());
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void bookAppointment_AppointmentExpired_ExceptionThrown() {
        //given
        Doctor doctor = Doctor.builder().id(1L).build();
        Long patientId = 2L;
        Appointment appointment = Appointment.builder()
                .id(3L)
                .startTime(LocalDateTime.of(2000, 9, 29, 10, 0))
                .endTime(LocalDateTime.of(2000, 9, 29, 10, 30))
                .doctor(doctor)
                .patient(null)
                .build();

        when(appointmentRepository.findById(3L)).thenReturn(Optional.of(appointment));
        //when
        AppointmentExpiredException exception = Assertions.assertThrowsExactly(AppointmentExpiredException.class,
                () -> appointmentService.bookAppointment(3L, patientId));
        //then
        Assertions.assertAll(
                () -> assertEquals("Cannot sign up to appointment in the past", exception.getMessage()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus())
        );

        verify(appointmentRepository).findById(3L);
        verify(patientRepository, never()).findById(anyLong());
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }
}
