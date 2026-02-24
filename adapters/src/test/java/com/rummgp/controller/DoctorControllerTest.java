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
public class DoctorControllerTest {
    @MockitoBean
    private DoctorService doctorService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnPagedDoctorDtosWhenDataCorrect() throws Exception {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        Doctor doctor1 = Doctor.builder()
                .id(3L)
                .firstName("doctorFirstName1")
                .lastName("doctorLastName1")
                .specialization("doctorSpecialization1")
                .user(user1)
                .institutions(new ArrayList<>())
                .appointments(new ArrayList<>())
                .build();
        Doctor doctor2 = Doctor.builder()
                .id(4L)
                .firstName("doctorFirstName2")
                .lastName("doctorLastName2")
                .specialization("doctorSpecialization2")
                .user(user2)
                .institutions(new ArrayList<>())
                .appointments(new ArrayList<>())
                .build();
        DoctorFindCommand doctorFindCommand = DoctorFindCommand.builder()
                .pageNumber(0)
                .pageSize(20)
                .build();
        PagePojo<Doctor> page = new PagePojo<>(List.of(doctor1, doctor2), doctorFindCommand.pageNumber(), doctorFindCommand.pageSize(), 2, 2);

        when(doctorService.find(doctorFindCommand)).thenReturn(page);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pageNumber", String.valueOf(doctorFindCommand.pageNumber()))
                        .param("pageSize", String.valueOf(doctorFindCommand.pageSize()))
        )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content", hasSize(2)),
                        jsonPath("$.content[0].id").value(3L),
                        jsonPath("$.content[0].firstName").value("doctorFirstName1"),
                        jsonPath("$.content[0].lastName").value("doctorLastName1"),
                        jsonPath("$.content[0].user.id").value(1L),
                        jsonPath("$.content[0].appointmentsId", hasSize(0)),
                        jsonPath("$.content[0].institutionsId", hasSize(0))
                );
    }

    @Test
    void shouldSaveAndReturnDoctorDtoWhenDataCorrect() throws Exception {
        UserCreateCommand inputUser = UserCreateCommand.builder().id(1L).build();
        User user = User.builder().id(1L).build();
        DoctorCreateCommand inputDoctor = DoctorCreateCommand.builder()
                .firstName("firstName")
                .lastName("lastName")
                .specialization("specialization")
                .user(inputUser)
                .build();
        Doctor doctor = Doctor.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .specialization("specialization")
                .user(user)
                .appointments(new ArrayList<>())
                .institutions(new ArrayList<>())
                .build();

        when(doctorService.add(any())).thenReturn(doctor);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDoctor))
        )
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.firstName").value("firstName"),
                        jsonPath("$.lastName").value("lastName"),
                        jsonPath("$.specialization").value("specialization"),
                        jsonPath("$.user.id").value(1L),
                        jsonPath("$.appointmentsId", hasSize(0)),
                        jsonPath("$.institutionsId", hasSize(0))
                );
    }

    @Test
    void shouldReturnDoctorDtoWithAssignedInstitutionWhenDataCorrect() throws Exception {
        User user = User.builder().id(1L).build();
        Institution institution = Institution.builder().id(3L).build();
        Doctor doctor = Doctor.builder()
                .id(2L)
                .firstName("firstName")
                .lastName("lastName")
                .specialization("specialization")
                .user(user)
                .appointments(new ArrayList<>())
                .institutions(List.of(institution))
                .build();

        when(doctorService.assignInstitutionToDoctor(doctor.getId(), institution.getId())).thenReturn(doctor);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/doctors/2/institutions/3")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(2L),
                        jsonPath("$.firstName").value("firstName"),
                        jsonPath("$.lastName").value("lastName"),
                        jsonPath("$.specialization").value("specialization"),
                        jsonPath("$.user.id").value(1L),
                        jsonPath("$.appointmentsId", hasSize(0)),
                        jsonPath("$.institutionsId", hasSize(1)),
                        jsonPath("$.institutionsId[0]").value(3L)
                );
    }
}
