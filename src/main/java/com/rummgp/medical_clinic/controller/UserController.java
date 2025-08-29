package com.rummgp.medical_clinic.controller;

import com.rummgp.medical_clinic.command.ChangePasswordCommand;
import com.rummgp.medical_clinic.command.UserCreateCommand;
import com.rummgp.medical_clinic.dto.ErrorMessageDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "UserOperation")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users list returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
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
    public UserDto getPatientByEmail(@PathVariable Long id) {
        return userMapper.toDto(userService.getUser(id));
    }

    @Operation(summary = "Add user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User has been created.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "500", description = "Fields should not be null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserCreateCommand user) {
        return userMapper.toDto(userService.addUser(userMapper.toEntity(user)));
    }

    @Operation(summary = "Delete user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User has been deleted.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserById(@PathVariable Long id) {
        userService.removeUser(id);
    }

    @Operation(summary = "Change password using id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password has been changed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Fields should not be null",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PatchMapping("/{id}")
    public UserDto changePassword(@PathVariable Long id, @RequestBody ChangePasswordCommand newPassword) {
        return userMapper.toDto(userService.changeUserPassword(id, newPassword.password()));
    }
}
