package com.rummgp.medical_clinic.controller;

import com.rummgp.medical_clinic.command.InstitutionCreateCommand;
import com.rummgp.medical_clinic.dto.ErrorMessageDto;
import com.rummgp.medical_clinic.dto.InstitutionDto;
import com.rummgp.medical_clinic.mapper.InstitutionMapper;
import com.rummgp.medical_clinic.service.InstitutionService;
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
@RequestMapping("/institutions")
@Tag(name = "InstitutionOperations")
public class InstitutionController {
    private final InstitutionService institutionService;
    private final InstitutionMapper institutionMapper;

    @Operation(summary = "Get all Institutions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Institutions list returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = InstitutionDto.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @GetMapping
    public List<InstitutionDto> getInstitutions() {
        return institutionService.getAll().stream()
                .map(institutionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Add institution")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Institution has been created.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstitutionDto.class))}),
            @ApiResponse(responseCode = "409", description = "Institution with this name already exists",
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
    public InstitutionDto addInstitution(@RequestBody InstitutionCreateCommand institution) {
        return institutionMapper.toDto(institutionService.addInstitution(institutionMapper.toEntity(institution)));
    }
}
