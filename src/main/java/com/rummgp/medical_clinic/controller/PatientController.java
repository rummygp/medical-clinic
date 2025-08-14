package com.rummgp.medical_clinic.controller;

import com.rummgp.medical_clinic.command.ChangePasswordCommand;
import com.rummgp.medical_clinic.dto.ErrorMessageDto;
import com.rummgp.medical_clinic.dto.PatientDto;
import com.rummgp.medical_clinic.mapper.PatientMapper;
import com.rummgp.medical_clinic.model.Patient;
import com.rummgp.medical_clinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
@Tag(name = "PatientOperation")
public class PatientController {
    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @Operation(summary = "Get all patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients list returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientDto.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @GetMapping
    public List<PatientDto> getPatients() {
        return patientService.getAll().stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get patient by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient returned.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @GetMapping("/{email}")
    public PatientDto getPatientByEmail(@PathVariable String email) {
        return patientMapper.toDto(patientService.getPatient(email));
    }

    @Operation(summary = "Add patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient has been created.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "409", description = "Email already exists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Fields should not be null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto addPatient(@RequestBody Patient patient) {
        return patientMapper.toDto(patientService.addPatient(patient));
    }

    @Operation(summary = "Delete patient by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient has been deleted.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePatientByEmail(@PathVariable String email) {
        patientService.removePatient(email);
    }

    @Operation(summary = "Edit patient using email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient has been edited successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = "Email already exists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Fields should not be null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Id Card number can't be changed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PutMapping("/{email}")
    public PatientDto editPatient(@PathVariable String email, @RequestBody Patient patient) {
        return patientMapper.toDto(patientService.editPatient(email, patient));
    }

    @Operation(summary = "Change password using email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password has been changed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Fields should not be null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PatchMapping("/{email}")
    public PatientDto changePassword(@PathVariable String email, @RequestBody ChangePasswordCommand password) {
        return patientMapper.toDto(patientService.changePassword(email, password.getPassword()));
    }
}
