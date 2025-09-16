package com.rummgp.medical_clinic.controller;

import com.rummgp.medical_clinic.command.ChangePasswordCommand;
import com.rummgp.medical_clinic.command.UserCreateCommand;
import com.rummgp.medical_clinic.dto.ErrorMessageDto;
import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.dto.UserDto;
import com.rummgp.medical_clinic.mapper.UserMapper;
import com.rummgp.medical_clinic.service.UserService;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "UserOperation")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Page of users returned")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users page returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @GetMapping
    public PageDto<UserDto> findAll(@ParameterObject Pageable pageable) {
        return userService.findAll(pageable);
    }

    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User returned.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @GetMapping("/{id}")
    public UserDto find(@PathVariable Long id) {
        return userMapper.toDto(userService.find(id));
    }

    @Operation(summary = "Add user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User has been created.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "409", description = "User with this username already exists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "400", description = "Fields should not be null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@RequestBody UserCreateCommand user) {
        return userMapper.toDto(userService.add(userMapper.toEntity(user)));
    }

    @Operation(summary = "Change password using id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password has been changed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "400", description = "Fields should not be null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PatchMapping("/{id}")
    public UserDto changePassword(@PathVariable Long id, @RequestBody ChangePasswordCommand newPassword) {
        return userMapper.toDto(userService.changePassword(id, newPassword.password()));
    }
}
