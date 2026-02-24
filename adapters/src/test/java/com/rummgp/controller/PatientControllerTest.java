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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {
    @MockitoBean
    private PatientService patientService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnPatientDtosWhenDataCorrect() throws Exception {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        Patient patient1 = Patient.builder()
                .id(3L)
                .firstName("firstName1")
                .lastName("lastName1")
                .phoneNumber("phoneNumber1")
                .birthday(LocalDate.of(2001, 1, 11))
                .user(user1)
                .appointments(new ArrayList<>())
                .build();
        Patient patient2 = Patient.builder()
                .id(4L)
                .firstName("firstName2")
                .lastName("lastName2")
                .phoneNumber("phoneNumber2")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(user2)
                .appointments(new ArrayList<>())
                .build();
        PatientFindCommand patientFindCommand = PatientFindCommand.builder()
                .pageNumber(0)
                .pageSize(20)
                .build();
        PagePojo<Patient> page = new PagePojo<>(List.of(patient1, patient2), patientFindCommand.pageNumber(), patientFindCommand.pageSize(), 2, 1);

        when(patientService.findAll(patientFindCommand)).thenReturn(page);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", String.valueOf(patientFindCommand.pageNumber()))
                                .param("pageSize", String.valueOf(patientFindCommand.pageSize()))
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content", hasSize(2)),
                        jsonPath("$.content[0].id").value(3L),
                        jsonPath("$.content[0].firstName").value("firstName1"),
                        jsonPath("$.content[0].lastName").value("lastName1"),
                        jsonPath("$.content[0].phoneNumber").value("phoneNumber1"),
                        jsonPath("$.content[0].birthday").value("2001-01-11"),
                        jsonPath("$.content[0].user.id").value(1L),
                        jsonPath("$.content[0].appointmentsId", hasSize(0))
                );
    }

    @Test
    void shouldReturnPatientDtoWhenDataCorrect() throws Exception {
        User user = User.builder().id(1L).build();
        Patient patient = Patient.builder()
                .id(2L)
                .firstName("patientFirstName")
                .lastName("patientLastName")
                .idCardNo("patientIdCardNo")
                .phoneNumber("patientPhoneNumber")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(user)
                .appointments(new ArrayList<>())
                .build();

        when(patientService.find(patient.getId())).thenReturn(patient);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/patients/2")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(2L),
                        jsonPath("$.firstName").value("patientFirstName"),
                        jsonPath("$.lastName").value("patientLastName"),
                        jsonPath("$.idCardNo").doesNotExist(),
                        jsonPath("$.phoneNumber").value("patientPhoneNumber"),
                        jsonPath("$.birthday").value("2002-02-22"),
                        jsonPath("$.user.id").value(1L),
                        jsonPath("$.appointmentsId", hasSize(0))
                );
    }

    @Test
    void shouldSaveAndReturnPatientDtoWhenDataCorrect() throws Exception {
        User user = User.builder().id(1L).build();
        UserCreateCommand inputUser = UserCreateCommand.builder().id(1L).build();
        PatientCreateCommand inputPatient = PatientCreateCommand.builder()
                .firstName("patientFirstName")
                .lastName("patientLastName")
                .idCardNo("patientIdCardNo")
                .phoneNumber("patientPhoneNumber")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(inputUser)
                .build();
        Patient patient = Patient.builder()
                .id(2L)
                .firstName("patientFirstName")
                .lastName("patientLastName")
                .idCardNo("patientIdCardNo")
                .phoneNumber("patientPhoneNumber")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(user)
                .appointments(new ArrayList<>())
                .build();

        when(patientService.add(any())).thenReturn(patient);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputPatient))
                )
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(2L),
                        jsonPath("$.firstName").value("patientFirstName"),
                        jsonPath("$.lastName").value("patientLastName"),
                        jsonPath("$.idCardNo").doesNotExist(),
                        jsonPath("$.phoneNumber").value("patientPhoneNumber"),
                        jsonPath("$.birthday").value("2002-02-22"),
                        jsonPath("$.user.id").value(1L),
                        jsonPath("$.appointmentsId", hasSize(0))
                );
    }

    @Test
    void shouldDeletePatientDtoWhenIdProvided() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/patients/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().isNoContent()
                );

        verify(patientService).delete(1L);
    }

    @Test
    void shouldEditAndReturnPatientDtoWhenValidDataProvided() throws Exception {
        User user = User.builder().id(1L).build();
        UserCreateCommand inputUser = UserCreateCommand.builder().id(1L).build();
        PatientCreateCommand inputPatient = PatientCreateCommand.builder()
                .firstName("patientFirstName")
                .lastName("patientLastName")
                .idCardNo("patientIdCardNo")
                .phoneNumber("patientPhoneNumber")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(inputUser)
                .build();
        Patient patient = Patient.builder()
                .id(2L)
                .firstName("patientFirstName")
                .lastName("patientLastName")
                .idCardNo("patientIdCardNo")
                .phoneNumber("patientPhoneNumber")
                .birthday(LocalDate.of(2002, 2, 22))
                .user(user)
                .appointments(new ArrayList<>())
                .build();

        when(patientService.edit(eq(patient.getId()), any(Patient.class))).thenReturn(patient);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/patients/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputPatient))
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(2L),
                        jsonPath("$.firstName").value("patientFirstName"),
                        jsonPath("$.lastName").value("patientLastName"),
                        jsonPath("$.idCardNo").doesNotExist(),
                        jsonPath("$.phoneNumber").value("patientPhoneNumber"),
                        jsonPath("$.birthday").value("2002-02-22"),
                        jsonPath("$.user.id").value(1L),
                        jsonPath("$.appointmentsId", hasSize(0))
                );

        verify(patientService).edit(eq(2L), any(Patient.class));
    }
}
