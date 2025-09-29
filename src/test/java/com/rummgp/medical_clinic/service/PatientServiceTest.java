package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.dto.PatientDto;
import com.rummgp.medical_clinic.exception.FieldsShouldNotBeNullException;
import com.rummgp.medical_clinic.exception.ImmutableFieldException;
import com.rummgp.medical_clinic.exception.NotFoundException;
import com.rummgp.medical_clinic.mapper.PageMapper;
import com.rummgp.medical_clinic.mapper.PatientMapper;
import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.model.User;
import com.rummgp.medical_clinic.repository.PatientRepository;
import com.rummgp.medical_clinic.repository.UserRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PatientServiceTest {
    private PatientRepository patientRepository;
    private UserRepository userRepository;
    private PatientMapper patientMapper;
    private PageMapper pageMapper;
    private PatientService patientService;

    @BeforeEach
    void setup() {
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        this.pageMapper = Mappers.getMapper(PageMapper.class);
        this.patientService = new PatientService(patientRepository, patientMapper, userRepository, pageMapper);
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
        Pageable pageable = PageRequest.of(0, 2);
        Page<Patient> page = new PageImpl<>(patients, pageable, patients.size());

        when(patientRepository.findAll(pageable)).thenReturn(page);
        //when
        PageDto<PatientDto> result = patientService.findAll(pageable);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.content().get(0).id()),
                () -> assertEquals("firstName1", result.content().get(0).firstName()),
                () -> assertEquals("lastName1", result.content().get(0).lastName()),
                () -> assertEquals("phoneNumber1", result.content().get(0).phoneNumber()),
                () -> assertEquals(LocalDate.of(2002, 2, 22), result.content().get(0).birthday()),
                () -> assertEquals(1L, result.content().get(0).user().id()),
                () -> assertEquals("email1", result.content().get(0).user().email()),
                () -> assertEquals("username1", result.content().get(0).user().username()),
                () -> assertTrue(result.content().get(0).appointmentsId().isEmpty()),

                () -> assertEquals(2L, result.content().get(1).id()),
                () -> assertEquals("firstName2", result.content().get(1).firstName()),
                () -> assertEquals("lastName2", result.content().get(1).lastName()),
                () -> assertEquals("phoneNumber2", result.content().get(1).phoneNumber()),
                () -> assertEquals(LocalDate.of(2003, 2, 13), result.content().get(1).birthday()),
                () -> assertEquals(2L, result.content().get(1).user().id()),
                () -> assertEquals("email2", result.content().get(1).user().email()),
                () -> assertEquals("username2", result.content().get(1).user().username()),
                () -> assertTrue(result.content().get(1).appointmentsId().isEmpty())
        );

        verify(patientRepository).findAll(pageable);
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

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
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

        verify(patientRepository).findById(anyLong());
    }

    @Test
    void find_PatientNotFound_ExceptionThrown() {
        //given
        Long patientId = 1L;

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class, () -> patientService.find(patientId));
        //then
        Assertions.assertAll(
                () -> assertEquals("Patient with id: " + patientId + " doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );

        verify(patientRepository).findById(anyLong());
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

        when(patientRepository.save(patient)).thenReturn(patient);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
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

        verify(userRepository).findById(1L);
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

        when(patientRepository.save(inputPatient)).thenReturn(patient);
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

        verify(userRepository, never()).findById(1L);

        verify(patientRepository).save(any());
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
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus())
        );

        verify(patientRepository, never()).save(any());
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

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> patientService.add(patient));
        //then
        Assertions.assertAll(
                () -> assertEquals("User with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );

        verify(userRepository).findById(1L);

        verify(patientRepository,never()).save(any());
    }

    @Test
    void delete_IdProvided_PatientDeleted() {
        //given
        Patient patient = Patient.builder().id(1L).build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        //when
        patientService.delete(1L);
        //then
        verify(patientRepository).findById(1L);

        verify(patientRepository).delete(patient);
    }

    @Test
    void delete_PatientNotFound_ExceptionThrown() {
        //given
        Long patientId = 1L;

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> patientService.delete(patientId));
        //then
        Assertions.assertAll(
                () -> assertEquals("Patient with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );

        verify(patientRepository).findById(patientId);

        verify(patientRepository, never()).delete(any());
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

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        when(patientRepository.save(patient)).thenReturn(patient);
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

        verify(patientRepository).findById(1L);

        verify(patientRepository).save(patient);
    }

    @Test
    void edit_PatientNotFound_ExceptionThrown() {
        //given
        Patient patient = Patient.builder()
                .id(1L)
                .build();
        Patient updatedPatient = Patient.builder().id(2L).build();

        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> patientService.edit(1L, updatedPatient));
        //then
        Assertions.assertAll(
                () -> assertEquals("Patient with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );

        verify(patientRepository).findById(1L);

        verify(patientRepository, never()).save(any());
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

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        //when
        FieldsShouldNotBeNullException exception = Assertions.assertThrowsExactly(FieldsShouldNotBeNullException.class,
                () -> patientService.edit(1L, updatedPatient));
        //then
        Assertions.assertAll(
                () -> assertEquals("Fields should not be null", exception.getMessage()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus())
        );

        verify(patientRepository).findById(1L);

        verify(patientRepository, never()).save(any());
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

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        //when
        ImmutableFieldException exception = Assertions.assertThrowsExactly(ImmutableFieldException.class,
                () -> patientService.edit(1L, updatedPatient));
        //then
        Assertions.assertAll(
                () -> assertEquals("idCardNo field can't be changed", exception.getMessage()),
                () -> assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus())
        );

        verify(patientRepository).findById(1L);

        verify(patientRepository, never()).save(any());
    }
}
