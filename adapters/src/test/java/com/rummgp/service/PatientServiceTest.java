package com.rummgp.service;

import com.rummgp.*;
import com.rummgp.exception.FieldsShouldNotBeNullException;
import com.rummgp.exception.ImmutableFieldException;
import com.rummgp.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientServiceTest {
    private PatientRepositoryPort patientRepositoryPort;
    private UserRepositoryPort userRepositoryPort;
    private PatientService patientService;

    @BeforeEach
    void setup() {
        this.patientRepositoryPort = Mockito.mock(PatientRepositoryPort.class);
        this.userRepositoryPort = Mockito.mock(UserRepositoryPort.class);
        this.patientService = new PatientService(patientRepositoryPort, userRepositoryPort);
    }

    @Test
    void findAll_DataCorrect_PagePatientsReturned() {
        //given
        User user1 = User.builder()
                .id(1L)
                .email("email1")
                .username("username1")
                .password("password1")
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("email2")
                .username("username2")
                .password("password2")
                .build();
        Patient patient1 = Patient.builder()
                .id(1L)
                .firstName("firstName1")
                .lastName("lastName1")
                .idCardNo("idCardNo1")
                .phoneNumber("phoneNumber1")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(user1)
                .appointments(new ArrayList<>())
                .build();
        Patient patient2 = Patient.builder()
                .id(2L)
                .firstName("firstName2")
                .lastName("lastName2")
                .idCardNo("idCardNo2")
                .phoneNumber("phoneNumber2")
                .birthday(LocalDate.of(2003, 2, 13))
                .user(user2)
                .appointments(new ArrayList<>())
                .build();
        List<Patient> patients = List.of(patient1, patient2);
        PatientFindCommand patientFindCommand = PatientFindCommand.builder()
                .pageNumber(0)
                .pageSize(20)
                .build();
        PagePojo<Patient> patientsPage = new PagePojo<>(patients, 0, 20, 2L, 1);

        when(patientRepositoryPort.findAll(patientFindCommand)).thenReturn(patientsPage);
        //when
        PagePojo<Patient> result = patientService.findAll(patientFindCommand);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.content().get(0).getId()),
                () -> assertEquals("firstName1", result.content().get(0).getFirstName()),
                () -> assertEquals("lastName1", result.content().get(0).getLastName()),
                () -> assertEquals("phoneNumber1", result.content().get(0).getPhoneNumber()),
                () -> assertEquals(LocalDate.of(2002, 2, 22), result.content().get(0).getBirthday()),
                () -> assertEquals(1L, result.content().get(0).getUser().getId()),
                () -> assertEquals("email1", result.content().get(0).getUser().getEmail()),
                () -> assertEquals("username1", result.content().get(0).getUser().getUsername()),
                () -> assertTrue(result.content().get(0).getAppointments().isEmpty()),
                () -> assertEquals(2L, result.content().get(1).getId()),
                () -> assertEquals("firstName2", result.content().get(1).getFirstName()),
                () -> assertEquals("lastName2", result.content().get(1).getLastName()),
                () -> assertEquals("phoneNumber2", result.content().get(1).getPhoneNumber()),
                () -> assertEquals(LocalDate.of(2003, 2, 13), result.content().get(1).getBirthday()),
                () -> assertEquals(2L, result.content().get(1).getUser().getId()),
                () -> assertEquals("email2", result.content().get(1).getUser().getEmail()),
                () -> assertEquals("username2", result.content().get(1).getUser().getUsername()),
                () -> assertTrue(result.content().get(1).getAppointments().isEmpty())
        );

        verify(patientRepositoryPort).findAll(patientFindCommand);
    }

    @Test
    void find_DataCorrect_PatientReturned() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .username("username")
                .password("password")
                .build();
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .idCardNo("idCardNo")
                .phoneNumber("phoneNumber")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(user)
                .appointments(new ArrayList<>())
                .build();

        when(patientRepositoryPort.findById(1L)).thenReturn(Optional.of(patient));
        //when
        Patient result = patientService.find(1L);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("firstName", result.getFirstName()),
                () -> assertEquals("lastName", result.getLastName()),
                () -> assertEquals("idCardNo", result.getIdCardNo()),
                () -> assertEquals("phoneNumber", result.getPhoneNumber()),
                () -> assertEquals(LocalDate.of(2002, 2, 22), result.getBirthday()),
                () -> assertEquals(1L, result.getUser().getId()),
                () -> assertEquals("username", result.getUser().getUsername()),
                () -> assertEquals("email", result.getUser().getEmail()),
                () -> assertEquals("password", result.getUser().getPassword()),
                () -> assertTrue(result.getAppointments().isEmpty())
        );

        verify(patientRepositoryPort).findById(anyLong());
    }

    @Test
    void find_PatientNotFound_ExceptionThrown() {
        //given
        Long patientId = 1L;

        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class, () -> patientService.find(patientId));
        //then
        Assertions.assertAll(
                () -> assertEquals("Patient with id: " + patientId + " doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
        );

        verify(patientRepositoryPort).findById(anyLong());
    }

    @Test
    void add_WithExistingUser_PatientReturned() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .username("username")
                .password("password")
                .build();
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .idCardNo("idCardNo")
                .phoneNumber("phoneNumber")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(user)
                .appointments(new ArrayList<>())
                .build();

        when(patientRepositoryPort.save(patient)).thenReturn(patient);
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        //when
        Patient result = patientService.add(patient);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("firstName", result.getFirstName()),
                () -> assertEquals("lastName", result.getLastName()),
                () -> assertEquals("idCardNo", result.getIdCardNo()),
                () -> assertEquals("phoneNumber", result.getPhoneNumber()),
                () -> assertEquals(LocalDate.of(2002, 2 ,22), result.getBirthday()),
                () -> assertEquals(1L, result.getUser().getId()),
                () -> assertEquals("email", result.getUser().getEmail()),
                () -> assertEquals("username", result.getUser().getUsername()),
                () -> assertEquals("password", result.getUser().getPassword())
        );

        verify(userRepositoryPort).findById(1L);
    }

    @Test
    void add_AndCreateUser_PatientReturned() {
        //given
        User inputUser = User.builder().id(null).build();
        User user = User.builder()
                .id(1L)
                .email("email")
                .username("username")
                .password("password")
                .build();
        Patient inputPatient = Patient.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .idCardNo("idCardNo")
                .phoneNumber("phoneNumber")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(inputUser)
                .appointments(new ArrayList<>())
                .build();
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .idCardNo("idCardNo")
                .phoneNumber("phoneNumber")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(user)
                .appointments(new ArrayList<>())
                .build();

        when(patientRepositoryPort.save(inputPatient)).thenReturn(patient);
        //when
        Patient result = patientService.add(inputPatient);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("firstName", result.getFirstName()),
                () -> assertEquals("lastName", result.getLastName()),
                () -> assertEquals("idCardNo", result.getIdCardNo()),
                () -> assertEquals("phoneNumber", result.getPhoneNumber()),
                () -> assertEquals(LocalDate.of(2002, 2 ,22), result.getBirthday()),
                () -> assertEquals(1L, result.getUser().getId()),
                () -> assertEquals("email", result.getUser().getEmail()),
                () -> assertEquals("username", result.getUser().getUsername()),
                () -> assertEquals("password", result.getUser().getPassword())
        );

        verify(userRepositoryPort, never()).findById(1L);
        verify(patientRepositoryPort).save(any());
    }

    @Test
    void add_PatientFieldsShouldNotBeNull_ExceptionThrown() {
        //given
        Patient patient = Patient.builder().firstName(null).build();
        //when
        FieldsShouldNotBeNullException exception = Assertions.assertThrowsExactly(FieldsShouldNotBeNullException.class,
                () -> patientService.add(patient));
        //then
        Assertions.assertAll(
                () -> assertEquals("Fields should not be null", exception.getMessage()),
                () -> assertEquals(400, exception.getStatus())
        );

        verify(patientRepositoryPort, never()).save(any());
    }

    @Test
    void add_UserNotFound_ExceptionThrown() {
        //given
        User user = User.builder().id(1L).build();
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .idCardNo("idCardNo")
                .phoneNumber("phoneNumber")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(user)
                .appointments(new ArrayList<>())
                .build();

        when(userRepositoryPort.findById(1L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> patientService.add(patient));
        //then
        Assertions.assertAll(
                () -> assertEquals("User with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
        );

        verify(userRepositoryPort).findById(1L);
        verify(patientRepositoryPort,never()).save(any());
    }

    @Test
    void delete_IdProvided_PatientDeleted() {
        //given
        Patient patient = Patient.builder().id(1L).build();

        when(patientRepositoryPort.findById(1L)).thenReturn(Optional.of(patient));
        //when
        patientService.delete(1L);
        //then
        verify(patientRepositoryPort).findById(1L);
        verify(patientRepositoryPort).delete(patient);
    }

    @Test
    void delete_PatientNotFound_ExceptionThrown() {
        //given
        Long patientId = 1L;

        when(patientRepositoryPort.findById(patientId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> patientService.delete(patientId));
        //then
        Assertions.assertAll(
                () -> assertEquals("Patient with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
        );

        verify(patientRepositoryPort).findById(patientId);
        verify(patientRepositoryPort, never()).delete(any());
    }

    @Test
    void edit_UpdatedDataProvided_UpdatedPatientReturned() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .username("username")
                .password("password")
                .build();
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .idCardNo("idCardNo")
                .phoneNumber("phoneNumber")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(user)
                .appointments(new ArrayList<>())
                .build();
        Patient updatedPatient =  Patient.builder()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .phoneNumber("updatedPhoneNumber")
                .idCardNo("idCardNo")
                .birthday(LocalDate.of(2003, 5 ,17))
                .build();

        when(patientRepositoryPort.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepositoryPort.save(patient)).thenReturn(patient);
        //when
        Patient result = patientService.edit(1L, updatedPatient);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("updatedFirstName", result.getFirstName()),
                () -> assertEquals("updatedLastName", result.getLastName()),
                () -> assertEquals("idCardNo", result.getIdCardNo()),
                () -> assertEquals("updatedPhoneNumber", result.getPhoneNumber()),
                () -> assertEquals(LocalDate.of(2003, 5 ,17), result.getBirthday()),
                () -> assertEquals(1L, result.getUser().getId()),
                () -> assertEquals("email", result.getUser().getEmail()),
                () -> assertEquals("username", result.getUser().getUsername()),
                () -> assertEquals("password", result.getUser().getPassword()),
                () -> assertTrue(result.getAppointments().isEmpty())
        );

        verify(patientRepositoryPort).findById(1L);
        verify(patientRepositoryPort).save(patient);
    }

    @Test
    void edit_PatientNotFound_ExceptionThrown() {
        //given
        Patient patient = Patient.builder()
                .id(1L)
                .build();
        Patient updatedPatient = Patient.builder().id(2L).build();

        when(patientRepositoryPort.findById(1L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> patientService.edit(1L, updatedPatient));
        //then
        Assertions.assertAll(
                () -> assertEquals("Patient with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
        );

        verify(patientRepositoryPort).findById(1L);
        verify(patientRepositoryPort, never()).save(any());
    }

    @Test
    void edit_UpdatedPatientFieldsShouldNotBeNull_ExceptionThrown() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .username("username")
                .password("password")
                .build();
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .idCardNo("idCardNo")
                .phoneNumber("phoneNumber")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(user)
                .appointments(new ArrayList<>())
                .build();
        Patient updatedPatient = Patient.builder().firstName(null).build();

        when(patientRepositoryPort.findById(1L)).thenReturn(Optional.of(patient));
        //when
        FieldsShouldNotBeNullException exception = Assertions.assertThrowsExactly(FieldsShouldNotBeNullException.class,
                () -> patientService.edit(1L, updatedPatient));
        //then
        Assertions.assertAll(
                () -> assertEquals("Fields should not be null", exception.getMessage()),
                () -> assertEquals(400, exception.getStatus())
        );

        verify(patientRepositoryPort).findById(1L);
        verify(patientRepositoryPort, never()).save(any());
    }

    @Test
    void edit_ChangingImmutableField_ExceptionThrown() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .username("username")
                .password("password")
                .build();
        Patient patient = Patient.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .idCardNo("idCardNo")
                .phoneNumber("phoneNumber")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(user)
                .appointments(new ArrayList<>())
                .build();
        Patient updatedPatient =  Patient.builder()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .phoneNumber("updatedPhoneNumber")
                .idCardNo("updatedIdCardNo")
                .birthday(LocalDate.of(2003, 5 ,17))
                .build();

        when(patientRepositoryPort.findById(1L)).thenReturn(Optional.of(patient));
        //when
        ImmutableFieldException exception = Assertions.assertThrowsExactly(ImmutableFieldException.class,
                () -> patientService.edit(1L, updatedPatient));
        //then
        Assertions.assertAll(
                () -> assertEquals("idCardNo field can't be changed", exception.getMessage()),
                () -> assertEquals(409, exception.getStatus())
        );

        verify(patientRepositoryPort).findById(1L);
        verify(patientRepositoryPort, never()).save(any());
    }
}