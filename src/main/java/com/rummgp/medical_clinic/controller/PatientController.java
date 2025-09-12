package com.rummgp.medical_clinic.controller;

import com.rummgp.medical_clinic.command.PatientCreateCommand;
import com.rummgp.medical_clinic.dto.ErrorMessageDto;
import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.dto.PatientDto;
import com.rummgp.medical_clinic.mapper.PatientMapper;
import com.rummgp.medical_clinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/patients")
@Tag(name = "PatientOperation")
public class PatientController {
    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @Operation(summary = "Page of patients returned")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients page returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientDto.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @GetMapping
    public PageDto<PatientDto> findAll(Pageable pageable) {
        return patientService.findAll(pageable);
    }

    @Operation(summary = "Get patient by id")
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
    @GetMapping("/{id}")
    public PatientDto find(@PathVariable Long id) {
        return patientMapper.toDto(patientService.find(id));
    }

    @Operation(summary = "Add patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient has been created.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "400", description = "Fields should not be null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDto add(@RequestBody PatientCreateCommand patient) {
        return patientMapper.toDto(patientService.add(patientMapper.toEntity(patient)));
    }

    @Operation(summary = "Delete patient by id")
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        patientService.delete(id);
    }

    @Operation(summary = "Edit patient using id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient has been edited successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "400", description = "Fields should not be null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = "Id Card number can't be changed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PutMapping("/{id}")
    public PatientDto edit(@PathVariable Long id, @RequestBody PatientCreateCommand patient) {
        return patientMapper.toDto(patientService.edit(id, patientMapper.toEntity(patient)));
    }
}
