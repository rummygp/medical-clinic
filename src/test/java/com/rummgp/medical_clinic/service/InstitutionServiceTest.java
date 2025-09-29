package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.dto.InstitutionDto;
import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.exception.FieldsShouldNotBeNullException;
import com.rummgp.medical_clinic.exception.NameAlreadyExistsException;
import com.rummgp.medical_clinic.exception.NotFoundException;
import com.rummgp.medical_clinic.mapper.InstitutionMapper;
import com.rummgp.medical_clinic.mapper.PageMapper;
import com.rummgp.medical_clinic.model.Institution;
import com.rummgp.medical_clinic.repository.InstitutionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class InstitutionServiceTest {
    InstitutionRepository institutionRepository;
    InstitutionMapper institutionMapper;
    PageMapper pageMapper;
    InstitutionService institutionService;

    @BeforeEach()
    void setup() {
        this.institutionRepository = mock(InstitutionRepository.class);
        this.institutionMapper = Mappers.getMapper(InstitutionMapper.class);
        this.pageMapper = Mappers.getMapper(PageMapper.class);
        this.institutionService = new InstitutionService(institutionRepository, institutionMapper, pageMapper);
    }

    @Test
    void findAll_DataCorrect_InstitutionsReturned() {
        //given
        Institution institution1 = Institution.builder()
                .id(1L)
                .name("Institution 1")
                .city("City 1")
                .postalCode("Postal Code 1")
                .street("Street 1")
                .buildingNo("111")
                .doctors(new ArrayList<>())
                .build();
        Institution institution2 = Institution.builder()
                .id(2L)
                .name("Institution 2")
                .city("City 2")
                .postalCode("Postal Code 2")
                .street("Street 2")
                .buildingNo("222")
                .doctors(new ArrayList<>())
                .build();

        List<Institution> institutions = List.of(institution1, institution2);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Institution> page = new PageImpl<>(institutions, pageable, institutions.size());

        when(institutionRepository.findAll(pageable)).thenReturn(page);
        //when
        PageDto<InstitutionDto> result = institutionService.findAll(pageable);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.content().get(0).id()),
                () -> assertEquals("Institution 1", result.content().get(0).name()),
                () -> assertEquals("City 1", result.content().get(0).city()),
                () -> assertEquals("Postal Code 1", result.content().get(0).postalCode()),
                () -> assertEquals("Street 1", result.content().get(0).street()),
                () -> assertEquals("111", result.content().get(0).buildingNo()),
                () -> assertTrue(result.content().get(0).doctorsId().isEmpty()),

                () -> assertEquals(2L, result.content().get(1).id()),
                () -> assertEquals("Institution 2", result.content().get(1).name()),
                () -> assertEquals("City 2", result.content().get(1).city()),
                () -> assertEquals("Postal Code 2", result.content().get(1).postalCode()),
                () -> assertEquals("Street 2", result.content().get(1).street()),
                () -> assertEquals("222", result.content().get(1).buildingNo()),
                () -> assertTrue(result.content().get(1).doctorsId().isEmpty())
        );
    }

    @Test
    void find_DataCorrect_InstitutionReturned() {
        //given
        Institution institution1 = Institution.builder()
                .id(1L)
                .name("Institution 1")
                .city("City 1")
                .postalCode("Postal Code 1")
                .street("Street 1")
                .buildingNo("111")
                .doctors(new ArrayList<>())
                .build();

        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institution1));
        //when
        Institution result = institutionService.find(1L);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("Institution 1", result.getName()),
                () -> assertEquals("City 1", result.getCity()),
                () -> assertEquals("Postal Code 1", result.getPostalCode()),
                () -> assertEquals("Street 1", result.getStreet()),
                () -> assertEquals("111", result.getBuildingNo()),
                () -> assertTrue(result.getDoctors().isEmpty())
                );
    }

    @Test
    void find_InstitutionNotFound_ExceptionThrown() {
        Long institutionId = 1L;

        when(institutionRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> institutionService.find(1L));
        //then
        Assertions.assertAll(
                () -> assertEquals("Institution with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );
    }

    @Test
    void add_DataCorrect_InstitutionReturned() {
        //given
        Institution institution = Institution.builder()
                .id(1L)
                .name("institutionName")
                .city("institutionCity")
                .postalCode("institutionPostalCode")
                .street("institutionStreet")
                .buildingNo("institutionBuildingNo")
                .doctors(new ArrayList<>())
                .build();

        when(institutionRepository.save(institution)).thenReturn(institution);
        //when
        Institution result = institutionService.add(institution);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("institutionName", result.getName()),
                () -> assertEquals("institutionCity", result.getCity()),
                () -> assertEquals("institutionPostalCode", result.getPostalCode()),
                () -> assertEquals("institutionStreet", result.getStreet()),
                () -> assertEquals("institutionBuildingNo", result.getBuildingNo()),
                () -> assertTrue(result.getDoctors().isEmpty())
        );
    }

    @Test
    void add_InstitutionFieldsShouldNotBeNull_ExceptionThrown() {
        //given
        Institution institution = Institution.builder()
                .id(1L)
                .name(null)
                .city("institutionCity")
                .postalCode("institutionPostalCode")
                .street("institutionStreet")
                .buildingNo("institutionBuildingNo")
                .doctors(new ArrayList<>())
                .build();
        //when
        FieldsShouldNotBeNullException exception = Assertions.assertThrowsExactly(FieldsShouldNotBeNullException.class,
                () -> institutionService.add(institution));
        //then
        Assertions.assertAll(
                () -> assertEquals("Fields should not be null", exception.getMessage()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus())
        );

        verify(institutionRepository, never()).save(any());
    }

    @Test
    void add_InstitutionNameAlreadyExist_ExceptionThrown() {
        //given
        Institution institution = Institution.builder()
                .id(1L)
                .name("existingName")
                .city("institutionCity")
                .postalCode("institutionPostalCode")
                .street("institutionStreet")
                .buildingNo("institutionBuildingNo")
                .doctors(new ArrayList<>())
                .build();
        when(institutionRepository.findByName("existingName")).thenReturn(Optional.of(institution));
        //when
        NameAlreadyExistsException exception = Assertions.assertThrowsExactly(NameAlreadyExistsException.class,
                () -> institutionService.add(institution));
        //then
        Assertions.assertAll(
                () -> assertEquals("Institution with name: existingName already exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus())
        );

        verify(institutionRepository, never()).save(any());
    }

    @Test
    void delete_IdProvided_InstitutionDeleted() {
        //given
        Institution institution = Institution.builder().id(1L).build();

        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institution));
        //when
        institutionService.delete(1L);
        //then
        verify(institutionRepository).findById(1L);
        verify(institutionRepository, times(1)).delete(any());
    }

    @Test
    void delete_InstitutionNotFound_ExceptionThrown() {
        //given
        Long institutionId = 3L;

        when(institutionRepository.findById(3L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class, () -> institutionService.delete(3L));
        //then
        Assertions.assertAll(
                () -> assertEquals("Institution with id: 3 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );

        verify(institutionRepository, never()).delete(any());
    }

    @Test
    void update_UpdateInstitutionCorrect_InstitutionReturned() {
        //given
        Institution institution = Institution.builder()
                .id(1L)
                .name("institutionName")
                .city("institutionCity")
                .postalCode("institutionPostalCode")
                .street("institutionStreet")
                .buildingNo("institutionBuildingNo")
                .doctors(new ArrayList<>())
                .build();
        Institution updatedInstitution = Institution.builder()
                .name("updatedName")
                .city("updatedCity")
                .postalCode("updatedPostalCode")
                .street("updatedStreet")
                .buildingNo("updatedBuildingNo")
                .build();

        when(institutionRepository.findById(1L)).thenReturn(Optional.of(institution));

        when(institutionRepository.save(institution)).thenReturn(institution);
        //when
        Institution result = institutionService.update(1L, updatedInstitution);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("updatedName", result.getName()),
                () -> assertEquals("updatedCity", result.getCity()),
                () -> assertEquals("updatedPostalCode", result.getPostalCode()),
                () -> assertEquals("updatedStreet", result.getStreet()),
                () -> assertEquals("updatedBuildingNo", result.getBuildingNo()),
                () -> assertTrue(result.getDoctors().isEmpty())
        );

        verify(institutionRepository).findById(1L);
    }

    @Test
    void update_UpdatedInstitutionsFieldsShouldNotBeNull_ExceptionThrown() {
        //given
        Long institutionId = 1L;
        Institution updatedInstitution = Institution.builder()
                .name(null)
                .build();
        //when
        FieldsShouldNotBeNullException exception = Assertions.assertThrowsExactly(FieldsShouldNotBeNullException.class,
                () -> institutionService.update(1L, updatedInstitution));
        //then
        Assertions.assertAll(
                () -> assertEquals("Fields should not be null", exception.getMessage()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus())
        );

        verify(institutionRepository, never()).findById(any());

        verify(institutionRepository, never()).save(any());
    }

    @Test
    void update_InstitutionNotFound_ExceptionThrown() {
        //given
        Institution institution = Institution.builder()
                .id(1L)
                .name("institutionName")
                .city("institutionCity")
                .postalCode("institutionPostalCode")
                .street("institutionStreet")
                .buildingNo("institutionBuildingNo")
                .doctors(new ArrayList<>())
                .build();
        Institution updatedInstitution = Institution.builder()
                .name("updatedName")
                .city("updatedCity")
                .postalCode("updatedPostalCode")
                .street("updatedStreet")
                .buildingNo("updatedBuildingNo")
                .build();

        when(institutionRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class, () -> institutionService.update(1L, updatedInstitution));
        //then
        Assertions.assertAll(
                () -> assertEquals("Institution with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );

        verify(institutionRepository).findById(1L);

        verify(institutionRepository, never()).save(any());
    }
}
