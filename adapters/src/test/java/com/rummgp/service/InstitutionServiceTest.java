package com.rummgp.service;

import com.rummgp.*;
import com.rummgp.exception.FieldsShouldNotBeNullException;
import com.rummgp.exception.NameAlreadyExistsException;
import com.rummgp.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class InstitutionServiceTest {
    InstitutionRepositoryPort institutionRepositoryPort;
    InstitutionService institutionService;

    @BeforeEach()
    void setup() {
        this.institutionRepositoryPort = mock(InstitutionRepositoryPort.class);
        this.institutionService = new InstitutionService(institutionRepositoryPort);
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
        InstitutionFindCommand institutionFindCommand = InstitutionFindCommand.builder()
                .pageNumber(0)
                .pageSize(20)
                .build();
        PagePojo<Institution> institutionPagePojo = new PagePojo<>(institutions, 0, 20, 2L, 1);

        when(institutionRepositoryPort.findAll(institutionFindCommand)).thenReturn(institutionPagePojo);
        //when
        PagePojo<Institution> result = institutionService.findAll(institutionFindCommand);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.content().get(0).getId()),
                () -> assertEquals("Institution 1", result.content().get(0).getName()),
                () -> assertEquals("City 1", result.content().get(0).getCity()),
                () -> assertEquals("Postal Code 1", result.content().get(0).getPostalCode()),
                () -> assertEquals("Street 1", result.content().get(0).getStreet()),
                () -> assertEquals("111", result.content().get(0).getBuildingNo()),
                () -> assertTrue(result.content().get(0).getDoctors().isEmpty()),
                () -> assertEquals(2L, result.content().get(1).getId()),
                () -> assertEquals("Institution 2", result.content().get(1).getName()),
                () -> assertEquals("City 2", result.content().get(1).getCity()),
                () -> assertEquals("Postal Code 2", result.content().get(1).getPostalCode()),
                () -> assertEquals("Street 2", result.content().get(1).getStreet()),
                () -> assertEquals("222", result.content().get(1).getBuildingNo()),
                () -> assertTrue(result.content().get(1).getDoctors().isEmpty())
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

        when(institutionRepositoryPort.findById(1L)).thenReturn(Optional.of(institution1));
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

        when(institutionRepositoryPort.findById(1L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> institutionService.find(1L));
        //then
        Assertions.assertAll(
                () -> assertEquals("Institution with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
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

        when(institutionRepositoryPort.save(institution)).thenReturn(institution);
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
                () -> assertEquals(400, exception.getStatus())
        );

        verify(institutionRepositoryPort, never()).save(any());
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
        when(institutionRepositoryPort.findByName("existingName")).thenReturn(Optional.of(institution));
        //when
        NameAlreadyExistsException exception = Assertions.assertThrowsExactly(NameAlreadyExistsException.class,
                () -> institutionService.add(institution));
        //then
        Assertions.assertAll(
                () -> assertEquals("Institution with name: existingName already exist", exception.getMessage()),
                () -> assertEquals(409, exception.getStatus())
        );

        verify(institutionRepositoryPort, never()).save(any());
    }

    @Test
    void delete_IdProvided_InstitutionDeleted() {
        //given
        Institution institution = Institution.builder().id(1L).build();

        when(institutionRepositoryPort.findById(1L)).thenReturn(Optional.of(institution));
        //when
        institutionService.delete(1L);
        //then
        verify(institutionRepositoryPort).findById(1L);
        verify(institutionRepositoryPort, times(1)).delete(any());
    }

    @Test
    void delete_InstitutionNotFound_ExceptionThrown() {
        //given
        Long institutionId = 3L;

        when(institutionRepositoryPort.findById(3L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class, () -> institutionService.delete(3L));
        //then
        Assertions.assertAll(
                () -> assertEquals("Institution with id: 3 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
        );

        verify(institutionRepositoryPort, never()).delete(any());
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

        when(institutionRepositoryPort.findById(1L)).thenReturn(Optional.of(institution));
        when(institutionRepositoryPort.save(institution)).thenReturn(institution);
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

        verify(institutionRepositoryPort).findById(1L);
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
                () -> assertEquals(400, exception.getStatus())
        );

        verify(institutionRepositoryPort, never()).findById(any());
        verify(institutionRepositoryPort, never()).save(any());
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

        when(institutionRepositoryPort.findById(1L)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class, () -> institutionService.update(1L, updatedInstitution));
        //then
        Assertions.assertAll(
                () -> assertEquals("Institution with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
        );

        verify(institutionRepositoryPort).findById(1L);
        verify(institutionRepositoryPort, never()).save(any());
    }
}