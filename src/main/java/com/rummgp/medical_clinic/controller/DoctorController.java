package com.rummgp.medical_clinic.controller;

import com.rummgp.medical_clinic.command.DoctorCreateCommand;
import com.rummgp.medical_clinic.dto.DoctorDto;
import com.rummgp.medical_clinic.dto.ErrorMessageDto;
import com.rummgp.medical_clinic.dto.PageDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/doctors")
@Tag(name = "DoctorOperations")
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @Operation(summary = "Page of doctors returned")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctors page returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DoctorDto.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @GetMapping
    public PageDto<DoctorDto> findAll(Pageable pageable) {
        return doctorService.findAll(pageable);
    }

    @Operation(summary = "Add doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor has been created.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Fields should not be null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto add(@RequestBody DoctorCreateCommand doctor) {
        return doctorMapper.toDto(doctorService.add(doctorMapper.toEntity(doctor)));
    }

    @Operation(summary = "Add institution to doctor using their IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institution has been added to doctor successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor/Institution not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PutMapping("/{doctorId}/institutions/{institutionId}")
    public DoctorDto addInstitutionToDoctor(@PathVariable Long doctorId, @PathVariable Long institutionId) {
        return doctorMapper.toDto(doctorService.assignInstitutionToDoctor(doctorId, institutionId));
    }
}
