package com.rummgp.medical_clinic.controller;

import com.rummgp.medical_clinic.command.AppointmentCreateCommand;
import com.rummgp.medical_clinic.dto.AppointmentDto;
import com.rummgp.medical_clinic.dto.ErrorMessageDto;
import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.mapper.AppointmentMapper;
import com.rummgp.medical_clinic.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/appointments")
@Tag(name = "AppointmentOperations")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @Operation(summary = "Page of appointments returned")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments page returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AppointmentDto.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @GetMapping
    public PageDto<AppointmentDto> find(@RequestParam(required = false) Long doctorId,
                                        @RequestParam(required = false) Long patientId,
                                        @ParameterObject Pageable pageable) {
        return appointmentService.find(doctorId, patientId, pageable);
    }

    @Operation(summary = "Add empty appointment to doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empty appointment has been added to doctor successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid appointment time.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = "Overlapping appointment",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDto add(@RequestBody AppointmentCreateCommand appointment) {
        return appointmentMapper.toDto(appointmentService.add(appointment));
    }

    @Operation(summary = "Book appointment for patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The appointment has been booked successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Id not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = "The appointment is already booked",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PatchMapping("/{appointmentId}/patients/{patientId}")
    public AppointmentDto book(@PathVariable Long appointmentId, @PathVariable Long patientId) {
        return appointmentMapper.toDto(appointmentService.bookAppointment(appointmentId, patientId));
    }

    @Operation(summary = "Get available appointments")
    @GetMapping("/available")
    public PageDto<AppointmentDto> getAvailable(@RequestParam(required = false) Long doctorId,
                                                                  @RequestParam(required = false) String specialization,
                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                                  @ParameterObject Pageable pageable) {
        return appointmentService.findAvailable(doctorId, specialization, date, pageable);
    }
}
