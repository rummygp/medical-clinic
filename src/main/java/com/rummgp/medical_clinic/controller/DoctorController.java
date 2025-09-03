package com.rummgp.medical_clinic.controller;

import com.rummgp.medical_clinic.command.DoctorCreateCommand;
import com.rummgp.medical_clinic.dto.DoctorDto;
import com.rummgp.medical_clinic.dto.ErrorMessageDto;
import com.rummgp.medical_clinic.mapper.DoctorMapper;
import com.rummgp.medical_clinic.service.DoctorService;
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
@RequestMapping("/doctors")
@Tag(name = "DoctorOperations")
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @Operation(summary = "Get all Doctors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctors list returned",
            content = {@Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = DoctorDto.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @GetMapping
    public List<DoctorDto> getDoctors() {
        return doctorService.getAllDoctors().stream()
                .map(doctorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Add doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor has been created.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "500", description = "Fields should not be null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto addDoctor(@RequestBody DoctorCreateCommand doctor) {
        return doctorMapper.toDto(doctorService.addDoctor(doctorMapper.toEntity(doctor)));
    }

    @Operation(summary = "Add institution to doctor using their IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institution has been added to doctor successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Institution not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PostMapping("/{doctorId}/institutions/{institutionId}")
    public DoctorDto addInstitutionToDoctor(@PathVariable Long doctorId, @PathVariable Long institutionId) {
        return doctorMapper.toDto(doctorService.addInstitutionToDoctor(doctorId, institutionId));
    }
}
