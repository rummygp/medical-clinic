package com.rummgp.medical_clinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rummgp.medical_clinic.command.InstitutionCreateCommand;
import com.rummgp.medical_clinic.dto.InstitutionDto;
import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.dto.PatientDto;
import com.rummgp.medical_clinic.model.Institution;
import com.rummgp.medical_clinic.service.InstitutionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InstitutionControllerTest {
    @MockitoBean
    private InstitutionService institutionService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnInstitutionsWhenDataCorrect() throws Exception {
        InstitutionDto institutionDto1 = InstitutionDto.builder()
                .id(1L)
                .name("institutionName1")
                .city("institutionCity1")
                .postalCode("institutionPostalCode1")
                .street("institutionStreet1")
                .buildingNo("institutionBuildingNo1")
                .doctorsId(new ArrayList<>())
                .build();
        InstitutionDto institutionDto2 = InstitutionDto.builder()
                .id(2L)
                .name("institutionName2")
                .city("institutionCity2")
                .postalCode("institutionPostalCode2")
                .street("institutionStreet2")
                .buildingNo("institutionBuildingNo2")
                .doctorsId(new ArrayList<>())
                .build();
        Pageable pageable = PageRequest.of(0, 2);
        PageDto<InstitutionDto> page = new PageDto<>(List.of(institutionDto1, institutionDto2), pageable.getPageNumber(), pageable.getPageSize(), 2, 1);

        when(institutionService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/institutions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "2")
        )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content", hasSize(2)),
                        jsonPath("$.content[0].id").value(1L),
                        jsonPath("$.content[0].name").value("institutionName1"),
                        jsonPath("$.content[0].city").value("institutionCity1"),
                        jsonPath("$.content[0].postalCode").value("institutionPostalCode1"),
                        jsonPath("$.content[0].street").value("institutionStreet1"),
                        jsonPath("$.content[0].buildingNo").value("institutionBuildingNo1"),
                        jsonPath("$.content[0].doctorsId", hasSize(0))
                );
    }

    @Test
    void shouldSaveAndReturnInstitutionWhenDataCorrect() throws Exception {
        InstitutionCreateCommand inputInstitution = InstitutionCreateCommand.builder()
                .name("institutionName")
                .city("institutionCity")
                .postalCode("institutionPostalCode")
                .street("institutionStreet")
                .buildingNo("institutionBuildingNo")
                .build();
        Institution institution = Institution.builder()
                .id(1L)
                .name("institutionName")
                .city("institutionCity")
                .postalCode("institutionPostalCode")
                .street("institutionStreet")
                .buildingNo("institutionBuildingNo")
                .doctors(new ArrayList<>())
                .build();

        when(institutionService.add(any())).thenReturn(institution);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/institutions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputInstitution))
        )
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(1L),
                        jsonPath("$.name").value("institutionName"),
                        jsonPath("$.city").value("institutionCity"),
                        jsonPath("$.postalCode").value("institutionPostalCode"),
                        jsonPath("$.street").value("institutionStreet"),
                        jsonPath("$.buildingNo").value("institutionBuildingNo"),
                        jsonPath("$.doctorsId", hasSize(0))
                );
    }
}
