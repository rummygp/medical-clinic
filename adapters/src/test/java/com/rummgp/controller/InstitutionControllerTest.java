package com.rummgp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rummgp.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
    void shouldReturnInstitutionDtosWhenDataCorrect() throws Exception {
        Institution institution = Institution.builder()
                .id(1L)
                .name("institutionName1")
                .city("institutionCity1")
                .postalCode("institutionPostalCode1")
                .street("institutionStreet1")
                .buildingNo("institutionBuildingNo1")
                .doctors(new ArrayList<>())
                .build();
        InstitutionFindCommand institutionFindCommand = InstitutionFindCommand.builder()
                .pageNumber(0)
                .pageSize(20)
                .build();
        PagePojo<Institution> page = new PagePojo<>(List.of(institution), institutionFindCommand.pageNumber(), institutionFindCommand.pageSize(), 2, 1);

        when(institutionService.findAll(institutionFindCommand)).thenReturn(page);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/institutions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pageNumber", String.valueOf(institutionFindCommand.pageNumber()))
                        .param("pageSize", String.valueOf(institutionFindCommand.pageSize()))
        )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content", hasSize(1)),
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
    void shouldSaveAndReturnInstitutionDtoWhenDataCorrect() throws Exception {
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
