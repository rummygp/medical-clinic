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
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTest {
    @MockitoBean
    private AppointmentService appointmentService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnPagedAppointmentDtosWhenDataCorrect() throws Exception {
        Doctor doctor = Doctor.builder().id(1L).build();
        Patient patient = Patient.builder().id(2L).build();
        Appointment appointment = Appointment.builder()
                .id(3L)
                .startTime(LocalDateTime.of(3001, 1, 11, 12, 0))
                .endTime(LocalDateTime.of(3001, 1, 11, 12, 15))
                .doctor(doctor)
                .patient(patient)
                .build();
        AppointmentFindCommand appointmentFindCommand = AppointmentFindCommand.builder()
                .pageNumber(0)
                .pageSize(20)
                .build();
        PagePojo<Appointment> appointmentPagePojo = new PagePojo<>(List.of(appointment), appointmentFindCommand.pageNumber(), appointmentFindCommand.pageSize(), 2, 1);

        when(appointmentService.find(appointmentFindCommand)).thenReturn(appointmentPagePojo);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("pageNumber", String.valueOf(appointmentFindCommand.pageNumber()))
                                .param("pageSize", String.valueOf(appointmentFindCommand.pageSize()))
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content[0].id").value(3L),
                        jsonPath("$.content[0].startTime").value("3001-01-11T12:00:00"),
                        jsonPath("$.content[0].endTime").value("3001-01-11T12:15:00"),
                        jsonPath("$.content[0].doctorId").value(1L),
                        jsonPath("$.content[0].patientId").value(2L)
                );
    }

    @Test
    void shouldSaveAndReturnAppointmentDtoWhenDataCorrect() throws Exception {
        Doctor doctor = Doctor.builder().id(1L).build();
        AppointmentCreateCommand inputAppointment = AppointmentCreateCommand.builder()
                .startTime(LocalDateTime.of(3001, 1, 11, 12, 0))
                .endTime(LocalDateTime.of(3001, 1, 11, 12, 15))
                .doctorId(doctor.getId())
                .build();
        Appointment appointment = Appointment.builder()
                .id(2L)
                .startTime(LocalDateTime.of(3001, 1, 11, 12, 0))
                .endTime(LocalDateTime.of(3001, 1, 11, 12, 15))
                .doctor(doctor)
                .patient(null)
                .build();

        when(appointmentService.add(any())).thenReturn(appointment);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputAppointment))
                )
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(2L),
                        jsonPath("$.startTime").value("3001-01-11T12:00:00"),
                        jsonPath("$.endTime").value("3001-01-11T12:15:00"),
                        jsonPath("$.doctorId").value(1L),
                        jsonPath("$.patientId", nullValue())
                );
    }

    @Test
    void ShouldReturnAppointmentDtoWhenBookingIsSuccessful() throws Exception {
        Doctor doctor = Doctor.builder().id(1L).build();
        Patient patient = Patient.builder().id(2L).build();
        Appointment appointment = Appointment.builder()
                .id(3L)
                .startTime(LocalDateTime.of(3001, 1, 11, 12, 0))
                .endTime(LocalDateTime.of(3001, 1, 11, 12, 15))
                .doctor(doctor)
                .patient(patient)
                .build();

        when(appointmentService.bookAppointment(1L, 2L)).thenReturn(appointment);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/appointments/1/patients/2")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(3L),
                        jsonPath("$.startTime").value("3001-01-11T12:00:00"),
                        jsonPath("$.endTime").value("3001-01-11T12:15:00"),
                        jsonPath("$.doctorId").value(1L),
                        jsonPath("$.patientId").value(2L)
                );
    }
}
