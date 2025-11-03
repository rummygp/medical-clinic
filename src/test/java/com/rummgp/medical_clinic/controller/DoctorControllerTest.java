package com.rummgp.medical_clinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rummgp.medical_clinic.command.DoctorCreateCommand;
import com.rummgp.medical_clinic.command.UserCreateCommand;
import com.rummgp.medical_clinic.dto.DoctorDto;
import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.dto.UserDto;
import com.rummgp.medical_clinic.model.Doctor;
import com.rummgp.medical_clinic.model.Institution;
import com.rummgp.medical_clinic.model.User;
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

    //todo
//    @Test
//    void shouldReturnPagedDoctorDtosWhenDataCorrect() throws Exception {
//        UserDto userDto1 = UserDto.builder().id(1L).build();
//        UserDto userDto2 = UserDto.builder().id(2L).build();
//        DoctorDto doctor1 = DoctorDto.builder()
//                .id(3L)
//                .firstName("doctorFirstName1")
//                .lastName("doctorLastName1")
//                .specialization("doctorSpecialization1")
//                .user(userDto1)
//                .institutionsId(new ArrayList<>())
//                .appointmentsId(new ArrayList<>())
//                .build();
//        DoctorDto doctor2 = DoctorDto.builder()
//                .id(4L)
//                .firstName("doctorFirstName2")
//                .lastName("doctorLastName2")
//                .specialization("doctorSpecialization2")
//                .user(userDto2)
//                .institutionsId(new ArrayList<>())
//                .appointmentsId(new ArrayList<>())
//                .build();
//        Pageable pageable = PageRequest.of(0, 2);
//        PageDto<DoctorDto> page = new PageDto<>(List.of(doctor1, doctor2), pageable.getPageNumber(), pageable.getPageSize(), 2, 2);
//
//        when(doctorService.find(pageable)).thenReturn(page);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/doctors")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("page", "0")
//                        .param("size", "2")
//        )
//                .andExpectAll(
//                        status().isOk(),
//                        jsonPath("$.content", hasSize(2)),
//                        jsonPath("$.content[0].id").value(3L),
//                        jsonPath("$.content[0].firstName").value("doctorFirstName1"),
//                        jsonPath("$.content[0].lastName").value("doctorLastName1"),
//                        jsonPath("$.content[0].user.id").value(1L),
//                        jsonPath("$.content[0].appointmentsId", hasSize(0)),
//                        jsonPath("$.content[0].institutionsId", hasSize(0))
//                );
//    }

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
