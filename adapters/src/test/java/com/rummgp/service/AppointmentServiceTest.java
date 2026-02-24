package com.rummgp.service;

import com.rummgp.*;
import com.rummgp.exception.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {
    private AppointmentRepositoryPort appointmentRepositoryPort;
    private DoctorRepositoryPort doctorRepositoryPort;
    private PatientRepositoryPort patientRepositoryPort;
    private AppointmentService appointmentService;

    @BeforeEach
    void setup() {
        this.appointmentRepositoryPort = Mockito.mock(AppointmentRepositoryPort.class);
        this.doctorRepositoryPort = Mockito.mock(DoctorRepositoryPort.class);
        this.patientRepositoryPort = Mockito.mock(PatientRepositoryPort.class);
        this.appointmentService = new AppointmentService(appointmentRepositoryPort, doctorRepositoryPort, patientRepositoryPort);
    }

    @Test
    void add_DataCorrect_AppointmentReturned() {
        //given
        Doctor doctor = Doctor.builder().id(1L).firstName("Adam").build();
        AppointmentCreateCommand appointmentCreateCommand = AppointmentCreateCommand.builder()
                .startTime(LocalDateTime.of(3025, 9, 29, 10, 0))
                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
                .doctorId(1L)
                .build();
        Appointment appointment = Appointment.builder()
                .id(1L)
                .startTime(LocalDateTime.of(3025, 9, 29, 10, 0))
                .endTime(LocalDateTime.of(3025, 9, 29, 10, 30))
                .doctor(doctor)
                .build();

        when(doctorRepositoryPort.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepositoryPort.save(appointmentCreateCommand, doctor)).thenReturn(appointment);
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

        verify(doctorRepositoryPort).findById(1L);
        verify(appointmentRepositoryPort).save(any(AppointmentCreateCommand.class), eq(doctor));
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

        when(doctorRepositoryPort.findById(doctorId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> appointmentService.add(appointmentCreateCommand));
        //then
        Assertions.assertAll(
                () -> assertEquals("Doctor with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
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

        when(doctorRepositoryPort.findById(1L)).thenReturn(Optional.of(doctor));
        //when
        InvalidAppointmentTimeException exception = Assertions.assertThrowsExactly(InvalidAppointmentTimeException.class,
                () -> appointmentService.add(appointmentCreateCommand));
        //then
        Assertions.assertAll(
                () -> assertEquals("The appointment cannot be scheduled in the past", exception.getMessage()),
                () -> assertEquals(400, exception.getStatus())
        );

        verify(doctorRepositoryPort).findById(1L);
        verify(appointmentRepositoryPort, never()).save(any(AppointmentCreateCommand.class), eq(doctor));
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

        when(doctorRepositoryPort.findById(1L)).thenReturn(Optional.of(doctor));
        //when
        InvalidAppointmentTimeException exception = Assertions.assertThrowsExactly(InvalidAppointmentTimeException.class,
                () -> appointmentService.add(appointmentCreateCommand));
        //then
        Assertions.assertAll(
                () -> assertEquals("Appointment must start and end on a full quarter of an hour", exception.getMessage()),
                () -> assertEquals(400, exception.getStatus())
        );

        verify(doctorRepositoryPort).findById(1L);
        verify(appointmentRepositoryPort, never()).save(any(AppointmentCreateCommand.class), eq(doctor));
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

        when(doctorRepositoryPort.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepositoryPort.findOverlapping(1L, LocalDateTime.of(3025, 9, 29, 10, 0), LocalDateTime.of(3025, 9, 29, 10, 30)))
                .thenReturn(appointments);
        //when
        AppointmentOverlapException exception = Assertions.assertThrowsExactly(AppointmentOverlapException.class,
                () -> appointmentService.add(appointmentCreateCommand));
        //then
        Assertions.assertAll(
                () -> assertEquals("The appointment overlaps with another appointment of this doctor", exception.getMessage()),
                () -> assertEquals(409, exception.getStatus())
        );

        verify(doctorRepositoryPort).findById(1L);
        verify(appointmentRepositoryPort, never()).save(any(AppointmentCreateCommand.class), eq(doctor));
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

        when(appointmentRepositoryPort.findById(3L)).thenReturn(Optional.of(appointment));
        when(patientRepositoryPort.findById(2L)).thenReturn(Optional.of(patient));
        when(appointmentRepositoryPort.book(appointment)).thenAnswer(returnsFirstArg());
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

        verify(appointmentRepositoryPort).findById(3L);
        verify(patientRepositoryPort).findById(2L);
        verify(appointmentRepositoryPort).book(appointment);
    }

    @Test
    void bookAppointment_AppointmentNotFound_ExceptionThrown() {
        //given
        Long appointmentId = 1L;
        Long patientId = 2L;

        when(appointmentRepositoryPort.findById(appointmentId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> appointmentService.bookAppointment(appointmentId, patientId));
        //then
        Assertions.assertAll(
                () -> assertEquals("Appointment with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
        );

        verify(appointmentRepositoryPort).findById(appointmentId);
        verify(appointmentRepositoryPort, never()).book(any(Appointment.class));
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

        when(appointmentRepositoryPort.findById(3L)).thenReturn(Optional.of(appointment));
        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> appointmentService.bookAppointment(3L, patientId));
        //then
        Assertions.assertAll(
                () -> assertEquals("Patient with id: 2 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
        );

        verify(appointmentRepositoryPort).findById(3L);
        verify(patientRepositoryPort).findById(2L);
        verify(appointmentRepositoryPort, never()).book(any(Appointment.class));
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

        when(appointmentRepositoryPort.findById(4L)).thenReturn(Optional.of(appointment));
        //when
        AppointmentBookingException exception = Assertions.assertThrowsExactly(AppointmentBookingException.class,
                () -> appointmentService.bookAppointment(4L, 3L));
        //then
        Assertions.assertAll(
                () -> assertEquals("The appointment is already booked", exception.getMessage()),
                () -> assertEquals(409, exception.getStatus())
        );

        verify(appointmentRepositoryPort).findById(4L);
        verify(patientRepositoryPort, never()).findById(anyLong());
        verify(appointmentRepositoryPort, never()).book(any(Appointment.class));
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

        when(appointmentRepositoryPort.findById(3L)).thenReturn(Optional.of(appointment));
        //when
        AppointmentExpiredException exception = Assertions.assertThrowsExactly(AppointmentExpiredException.class,
                () -> appointmentService.bookAppointment(3L, patientId));
        //then
        Assertions.assertAll(
                () -> assertEquals("Cannot sign up to appointment in the past", exception.getMessage()),
                () -> assertEquals(400, exception.getStatus())
        );

        verify(appointmentRepositoryPort).findById(3L);
        verify(patientRepositoryPort, never()).findById(anyLong());
        verify(appointmentRepositoryPort, never()).book(any(Appointment.class));
    }

    @Test
    void find_ReturnsPageDto_Mapped() {
        // given
        Doctor doctor = Doctor.builder().id(1L).build();
        Patient patient = Patient.builder().id(2L).build();
        AppointmentFindCommand appointmentFindCommand = AppointmentFindCommand.builder()
                .startTime(LocalDateTime.of(3025, 9, 29, 11, 0))
                .endTime(LocalDateTime.of(3025, 9, 29, 11, 30))
                .doctorId(doctor.getId())
                .patientId(patient.getId())
                .pageSize(20)
                .pageNumber(0)
                .build();
        Appointment appointment = Appointment.builder()
                .startTime(LocalDateTime.of(3025, 9, 29, 11, 0))
                .endTime(LocalDateTime.of(3025, 9, 29, 11, 30))
                .doctor(doctor)
                .patient(patient)
                .build();
        PagePojo<Appointment> appointments = new PagePojo<>(List.of(appointment), appointmentFindCommand.pageNumber(), appointmentFindCommand.pageSize(),
                1L, 1);
        when(appointmentRepositoryPort.find(appointmentFindCommand)).thenReturn(appointments);
        when(doctorRepositoryPort.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepositoryPort.findById(2L)).thenReturn(Optional.of(patient));

        // when
        PagePojo<Appointment> result = appointmentService.find(appointmentFindCommand);

        // then
        Assertions.assertAll(
                () -> assertEquals(1, result.content().size()),
                () -> assertEquals(0, result.page()),
                () -> assertEquals(20, result.size()),
                () -> assertEquals(1L, result.totalElements()),
                () -> assertEquals(1, result.totalPages()),
                () -> assertEquals(1L, result.content().get(0).getDoctor().getId()),
                () -> assertEquals(2L, result.content().get(0).getPatient().getId())
        );

        verify(appointmentRepositoryPort).find(any(AppointmentFindCommand.class));
    }

    @Test
    void find_DoctorNotFound_ExceptionThrown() {
        // given
        AppointmentFindCommand appointmentFindCommand = AppointmentFindCommand.builder()
                .doctorId(1L)
                .build();
        when(doctorRepositoryPort.findById(appointmentFindCommand.doctorId())).thenReturn(Optional.empty());

        // when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> appointmentService.find(appointmentFindCommand));

        // then
        Assertions.assertAll(
                () -> assertEquals("Doctor with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
        );

        verify(doctorRepositoryPort).findById(appointmentFindCommand.doctorId());
        verify(appointmentRepositoryPort, never()).find(appointmentFindCommand);
    }

    @Test
    void find_PatientNotFound_ExceptionThrown() {
        // given
        AppointmentFindCommand appointmentFindCommand = AppointmentFindCommand.builder()
                .patientId(1L)
                .build();
        when(patientRepositoryPort.findById(appointmentFindCommand.patientId())).thenReturn(Optional.empty());

        // when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> appointmentService.find(appointmentFindCommand));

        // then
        Assertions.assertAll(
                () -> assertEquals("Patient with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
        );

        verify(patientRepositoryPort).findById(appointmentFindCommand.patientId());
        verify(appointmentRepositoryPort, never()).find(appointmentFindCommand);
    }

    @Test
    void delete_ExistingAppointment_Deletes() {
        // given
        Appointment appointment = Appointment.builder().id(3L).build();
        when(appointmentRepositoryPort.findById(3L)).thenReturn(Optional.of(appointment));

        // when
        appointmentService.delete(3L);

        // then
        verify(appointmentRepositoryPort).findById(3L);
        verify(appointmentRepositoryPort).delete(appointment);
    }

    @Test
    void delete_AppointmentNotFound_ExceptionThrown() {
        // given
        Long id = 4L;
        when(appointmentRepositoryPort.findById(id)).thenReturn(Optional.empty());

        // when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> appointmentService.delete(id));

        // then
        Assertions.assertAll(
                () -> assertEquals("Appointment with id: 4 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
        );

        verify(appointmentRepositoryPort).findById(id);
        verify(appointmentRepositoryPort, never()).delete(any(Appointment.class));
    }

}
