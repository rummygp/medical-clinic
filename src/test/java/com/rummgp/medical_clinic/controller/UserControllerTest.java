package com.rummgp.medical_clinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rummgp.medical_clinic.command.ChangePasswordCommand;
import com.rummgp.medical_clinic.command.UserCreateCommand;
import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.dto.UserDto;
import com.rummgp.medical_clinic.model.User;
import com.rummgp.medical_clinic.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockitoBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnUserDtosWhenDataCorrect() throws Exception {
        UserDto userDto1 = UserDto.builder()
                .id(1L)
                .username("username1")
                .email("email1")
                .build();
        UserDto userDto2 = UserDto.builder()
                .id(2L)
                .username("username2")
                .email("email2")
                .build();
        Pageable pageable = PageRequest.of(0, 2);
        PageDto<UserDto> page = new PageDto<>(List.of(userDto1, userDto2), pageable.getPageNumber(), pageable.getPageSize(), 2, 1);

        when(userService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "2")
        )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content[0].id").value(1),
                        jsonPath("$.content[0].username").value("username1"),
                        jsonPath("$.content[0].email").value("email1"),
                        jsonPath("$.content[1].id").value(2),
                        jsonPath("$.content[1].username").value("username2"),
                        jsonPath("$.content[1].email").value("email2")
                );
    }

    @Test
    void shouldReturnUserDtoWhenDataCorrect() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("userUsername")
                .email("userEmail")
                .password("userPassword")
                .build();

        when(userService.find(user.getId())).thenReturn(user);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.username").value("userUsername"),
                        jsonPath("$.email").value("userEmail"),
                        jsonPath("$.password").doesNotExist()
                );
    }

    @Test
    void shouldSaveAndReturnUserDtoWhenDataCorrect() throws Exception {
        UserCreateCommand userCreateCommand = UserCreateCommand.builder()
                .id(1L)
                .username("userUsername")
                .email("userEmail")
                .password("userPassword")
                .build();
        User user = User.builder()
                .id(1L)
                .username("userUsername")
                .email("userEmail")
                .password("userPassword")
                .build();

        when(userService.add(any(User.class))).thenReturn(user);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateCommand))
        )
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(1L),
                        jsonPath("$.username").value("userUsername"),
                        jsonPath("$.email").value("userEmail"),
                        jsonPath("$.password").doesNotExist()
                );
    }

    @Test
    void shouldReturnUserDtoWhenValidNewPasswordAndIdProvided() throws Exception {
        Long userId = 1L;
        User user = User.builder()
                .id(1L)
                .email("userEmail")
                .username("userUsername")
                .password("newPassword")
                .build();
        ChangePasswordCommand changePasswordCommand = ChangePasswordCommand.builder().password("newPassword").build();

        when(userService.changePassword(userId, changePasswordCommand.password())).thenReturn(user);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordCommand))
        )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1L),
                        jsonPath("$.username").value("userUsername"),
                        jsonPath("$.email").value("userEmail"),
                        jsonPath("$.password").doesNotExist()
                );

        verify(userService).changePassword(1L, "newPassword");
    }
}
