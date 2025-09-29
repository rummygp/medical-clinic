package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.dto.DoctorDto;
import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.exception.FieldsShouldNotBeNullException;
import com.rummgp.medical_clinic.exception.NotFoundException;
import com.rummgp.medical_clinic.mapper.DoctorMapper;
import com.rummgp.medical_clinic.mapper.PageMapper;
import com.rummgp.medical_clinic.model.Doctor;
import com.rummgp.medical_clinic.model.Institution;
import com.rummgp.medical_clinic.model.User;
import com.rummgp.medical_clinic.repository.DoctorRepository;
import com.rummgp.medical_clinic.repository.InstitutionRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoctorServiceTest {
    private DoctorRepository doctorRepository;
    private UserRepository userRepository;
    private InstitutionRepository institutionRepository;
    private DoctorMapper doctorMapper;
    private PageMapper pageMapper;
    private DoctorService doctorService;

    @BeforeEach
    void setup() {
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.institutionRepository = Mockito.mock(InstitutionRepository.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.pageMapper = Mappers.getMapper(PageMapper.class);
        this.doctorService = new DoctorService(doctorRepository, userRepository, institutionRepository, doctorMapper, pageMapper);
    }

    @Test
    void findAll_DataCorrect_DoctorsReturned() {
        // given
        User user1 = User.builder().id(1L).email("email").username("username").password("password").build();
        User user2 = User.builder().id(2L).email("email2").username("username2").password("password2").build();
        Doctor doctor1 = Doctor.builder()
                .id(1L)
                .firstName("name1")
                .lastName("lastName1")
                .specialization("dentist")
                .user(user1)
                .institutions(new ArrayList<>())
                .appointments(new ArrayList<>())
                .build();
        Doctor doctor2 = Doctor.builder()
                .id(2L)
                .firstName("name2")
                .lastName("lastName2")
                .specialization("cardiologist")
                .user(user2)
                .institutions(new ArrayList<>())
                .appointments(new ArrayList<>())
                .build();

        List<Doctor> doctors = List.of(doctor1, doctor2);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Doctor> page = new PageImpl<>(doctors, pageable, doctors.size());
        when(doctorRepository.findAll(pageable)).thenReturn(page);
        //when
        PageDto<DoctorDto> result = doctorService.findAll(pageable);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.content().get(0).id()),
                () -> assertEquals("name1", result.content().get(0).firstName()),
                () -> assertEquals("lastName1", result.content().get(0).lastName()),
                () -> assertEquals("dentist", result.content().get(0).specialization()),
                () -> assertEquals(1L, result.content().get(0).user().id()),
                () -> assertEquals("email", result.content().get(0).user().email()),
                () -> assertEquals("username", result.content().get(0).user().username()),
                () -> assertTrue(result.content().get(0).institutionsId().isEmpty()),
                () -> assertTrue(result.content().get(0).appointmentsId().isEmpty()),

                () -> assertEquals(2L, result.content().get(1).id()),
                () -> assertEquals("name2", result.content().get(1).firstName()),
                () -> assertEquals("lastName2", result.content().get(1).lastName()),
                () -> assertEquals("cardiologist", result.content().get(1).specialization()),
                () -> assertEquals(2L, result.content().get(1).user().id()),
                () -> assertEquals("email2", result.content().get(1).user().email()),
                () -> assertEquals("username2", result.content().get(1).user().username()),
                () -> assertTrue(result.content().get(1).institutionsId().isEmpty()),
                () -> assertTrue(result.content().get(1).appointmentsId().isEmpty())
        );
    }

    @Test
    void find_DataCorrect_DoctorReturned() {
        //given
        User user = User.builder().id(1L).email("email").username("username").password("password").build();
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("name")
                .lastName("lastName")
                .specialization("dentist")
                .user(user)
                .institutions(new ArrayList<>())
                .appointments(new ArrayList<>())
                .build();
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        //when
        Doctor result = doctorService.find(1L);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("name", result.getFirstName()),
                () -> assertEquals("lastName", result.getLastName()),
                () -> assertEquals("dentist", result.getSpecialization()),
                () -> assertEquals(1L, result.getUser().getId()),
                () -> assertEquals("email", result.getUser().getEmail()),
                () -> assertEquals("username", result.getUser().getUsername()),
                () -> assertTrue(result.getInstitutions().isEmpty()),
                () -> assertTrue(result.getAppointments().isEmpty())
        );
    }

    @Test
    void find_DoctorNotFound_ExceptionThrown() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> doctorService.find(1L));
        //then
        Assertions.assertAll(
                () -> assertEquals("Doctor with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );
    }

    @Test
    void add_WithExistingUser_DoctorReturned() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .username("username")
                .password("password")
                .build();
        User inputUser = User.builder().id(1L).build();
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("name1")
                .lastName("lastName1")
                .specialization("dentist")
                .user(inputUser)
                .institutions(new ArrayList<>())
                .appointments(new ArrayList<>())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(doctorRepository.save(doctor)).thenReturn(doctor);
        //when
        Doctor result1 = doctorService.add(doctor);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result1.getId()),
                () -> assertEquals("name1", result1.getFirstName()),
                () -> assertEquals("lastName1", result1.getLastName()),
                () -> assertEquals("dentist", result1.getSpecialization()),
                () -> assertEquals(1L, result1.getUser().getId()),
                () -> assertEquals("email", result1.getUser().getEmail()),
                () -> assertEquals("username", result1.getUser().getUsername()),
                () -> assertSame(user, result1.getUser()),
                () -> assertTrue(result1.getInstitutions().isEmpty()),
                () -> assertTrue(result1.getAppointments().isEmpty())
        );
        verify(userRepository).findById(1L);
    }

    @Test
    void add_UserNotFound_ExceptionThrown() {
        //given
        User user = User.builder().id(2L).build();
        Doctor doctor = Doctor.builder()
                .firstName("name")
                .lastName("lastName")
                .specialization("specialization")
                .user(user)
                .build();

        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> doctorService.add(doctor));
        //then
        Assertions.assertAll(
                () -> assertEquals("User with id: 2 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );
    }

    @Test
    void add_AndCreateUser_DoctorReturned() {
        User inputUser = User.builder().id(null).build();
        User user = User.builder()
                .id(1L)
                .email("email")
                .username("username")
                .password("password")
                .build();
        Doctor inputDoctor = Doctor.builder()
                .id(null)
                .firstName("name1")
                .lastName("lastName1")
                .specialization("dentist")
                .user(inputUser)
                .institutions(new ArrayList<>())
                .appointments(new ArrayList<>())
                .build();
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("name1")
                .lastName("lastName1")
                .specialization("dentist")
                .user(user)
                .institutions(new ArrayList<>())
                .appointments(new ArrayList<>())
                .build();

        when(doctorRepository.save(inputDoctor)).thenReturn(doctor);
        //when
        Doctor result = doctorService.add(inputDoctor);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("name1", result.getFirstName()),
                () -> assertEquals("lastName1", result.getLastName()),
                () -> assertEquals("dentist", result.getSpecialization()),
                () -> assertEquals(1L, result.getUser().getId()),
                () -> assertEquals("email", result.getUser().getEmail()),
                () -> assertEquals("username", result.getUser().getUsername()),
                () -> assertTrue(result.getInstitutions().isEmpty()),
                () -> assertTrue(result.getAppointments().isEmpty())
        );
    }

    @Test
    void add_FieldsShouldNotBeNull_ExceptionThrown() {
        //given
        Doctor doctor = Doctor.builder()
                .firstName(null)
                .build();
        //when
        FieldsShouldNotBeNullException exception = Assertions.assertThrows(FieldsShouldNotBeNullException.class, () -> doctorService.add(doctor));
        //then
        Assertions.assertAll(
                () -> assertEquals("Fields should not be null", exception.getMessage()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus())
        );
    }

    @Test
    void delete_IdProvided_DoctorDeleted() {
        //given
        Doctor doctor = Doctor.builder().id(1L).build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        //when
        doctorService.delete(1L);
        //then
        verify(doctorRepository).findById(1L);
        verify(doctorRepository).delete(doctor);
    }

    @Test
    void delete_DoctorNotFound_ExceptionThrown() {
        //given
        Doctor doctor = Doctor.builder().id(1L).build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> doctorService.delete(1L));
        //then
        Assertions.assertAll(
                () -> assertEquals("Doctor with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );
    }

    @Test
    void update_UpdatedDataProvided_DoctorReturned() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .username("username")
                .build();
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("name")
                .lastName("lastName")
                .specialization("specialization")
                .user(user)
                .appointments(new ArrayList<>())
                .institutions(new ArrayList<>())
                .build();
        Doctor updatedDoctor = Doctor.builder()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .specialization("updatedSpecialization")
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        when(doctorRepository.save(doctor)).thenReturn(doctor);
        //when
        Doctor result = doctorService.update(1L, updatedDoctor);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("updatedFirstName", result.getFirstName()),
                () -> assertEquals("updatedLastName", result.getLastName()),
                () -> assertEquals("updatedSpecialization", result.getSpecialization()),
                () -> assertEquals(1L, result.getUser().getId()),
                () -> assertEquals("email", result.getUser().getEmail()),
                () -> assertEquals("username", result.getUser().getUsername()),
                () -> assertTrue(result.getInstitutions().isEmpty()),
                () -> assertTrue(result.getAppointments().isEmpty())
        );
    }

    @Test
    void update_UpdatedFieldsShouldNotBeNull_ExceptionThrown() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .username("username")
                .build();
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("name")
                .lastName("lastName")
                .specialization("specialization")
                .user(user)
                .appointments(new ArrayList<>())
                .institutions(new ArrayList<>())
                .build();
        Doctor updatedDoctor = Doctor.builder().firstName(null).build();
        //when
        FieldsShouldNotBeNullException exception = Assertions.assertThrows(FieldsShouldNotBeNullException.class,
                () -> doctorService.update(1L, updatedDoctor));
        //then
        Assertions.assertAll(
                () -> assertEquals("Fields should not be null", exception.getMessage()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus())
        );
    }

    @Test
    void update_DoctorNotFound_ExceptionThrown() {
        //given
        Doctor updatedDoctor = Doctor.builder()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .specialization("updatedSpecialization")
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> doctorService.update(1L, updatedDoctor));
        //then
        Assertions.assertAll(
                () -> assertEquals("Doctor with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );
    }

    @Test
    void assignInstitutionToDoctor_DataCorrect_DoctorReturned() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .username("username")
                .build();
        Doctor doctor = Doctor.builder()
                .id(2L)
                .firstName("name")
                .lastName("lastName")
                .specialization("specialization")
                .user(user)
                .appointments(new ArrayList<>())
                .institutions(new ArrayList<>())
                .build();
        Institution institution = Institution.builder()
                .id(3L)
                .name("institutionName")
                .city("city")
                .postalCode("postalCode")
                .street("street")
                .buildingNo("buildingNo")
                .doctors(new ArrayList<>())
                .build();

        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));

        when(institutionRepository.findById(3L)).thenReturn(Optional.of(institution));

        when(doctorRepository.save(doctor)).thenReturn(doctor);
        //when
        Doctor result = doctorService.assignInstitutionToDoctor(2L, 3L);
        //then
        Assertions.assertAll(
                () -> assertEquals(2L, result.getId()),
                () -> assertEquals("name", result.getFirstName()),
                () -> assertEquals("lastName", result.getLastName()),
                () -> assertEquals("specialization", result.getSpecialization()),
                () -> assertEquals(1L, result.getUser().getId()),
                () -> assertEquals("email", result.getUser().getEmail()),
                () -> assertEquals("username", result.getUser().getUsername()),
                () -> assertTrue(result.getInstitutions().contains(institution)),
                () -> assertTrue(result.getAppointments().isEmpty())
        );

        verify(doctorRepository).findById(any(Long.class));

        verify(institutionRepository).findById(any(Long.class));

        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void assignInstitutionToDoctor_DoctorNotFound_ExceptionReturned() {
        //given
        Long doctorId = 2L;
        Long institutionId = 3L;

        when(doctorRepository.findById(2L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> doctorService.assignInstitutionToDoctor(2L, 3L));
        //then
        Assertions.assertAll(
                () -> assertEquals("Doctor with id: 2 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );
    }

    @Test
    void assignInstitutionToDoctor_InstitutionNotFound_ExceptionReturned() {
        //given
        User user = User.builder()
                .id(1L)
                .email("email")
                .username("username")
                .build();
        Doctor doctor = Doctor.builder()
                .id(2L)
                .firstName("name")
                .lastName("lastName")
                .specialization("specialization")
                .user(user)
                .appointments(new ArrayList<>())
                .institutions(new ArrayList<>())
                .build();
        Long institutionId = 3L;

        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));

        when(institutionRepository.findById(3L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class, () -> doctorService.assignInstitutionToDoctor(2L, 3L));
        //then
        Assertions.assertAll(
                () -> assertEquals("Institution with id: 3 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );

        verify(doctorRepository, never()).save(any());
    }
}
