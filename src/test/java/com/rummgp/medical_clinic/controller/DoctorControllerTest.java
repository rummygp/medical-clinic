package com.rummgp.medical_clinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rummgp.medical_clinic.dto.DoctorDto;
import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.service.DoctorService;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {
    @MockitoBean
    private DoctorService doctorService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnDoctorsWhenDataCorrect() throws Exception {
        DoctorDto doctor1 = DoctorDto.builder().firstName("Adam").build();
        DoctorDto doctor2 = DoctorDto.builder().firstName("Paweł").build();
        Pageable pageable = PageRequest.of(0, 2);
        PageDto<DoctorDto> page = new PageDto<>(List.of(doctor1, doctor2), pageable.getPageNumber(), pageable.getPageSize(), 2, 2);
        when(doctorService.findAll(pageable)).thenReturn(page);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0").param("size", "2")
        )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content[0].firstName").value("Adam"),
                        jsonPath("$.content[1].firstName").value("Paweł")
                );
    }
}
