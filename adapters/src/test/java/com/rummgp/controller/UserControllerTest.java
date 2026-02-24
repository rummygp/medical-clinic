package com.rummgp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rummgp.*;
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
        User userDto1 = User.builder()
                .id(1L)
                .username("username1")
                .email("email1")
                .build();
        UserFindCommand userFindCommand = UserFindCommand.builder()
                .pageNumber(0)
                .pageSize(20)
                .build();
        PagePojo<User> page = new PagePojo<>(List.of(userDto1), userFindCommand.pageNumber(), userFindCommand.pageSize(), 1, 1);

        when(userService.findAll(userFindCommand)).thenReturn(page);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pageNumber", String.valueOf(userFindCommand.pageNumber()))
                        .param("pageSize", String.valueOf(userFindCommand.pageSize()))
        )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content[0].id").value(1),
                        jsonPath("$.content[0].username").value("username1"),
                        jsonPath("$.content[0].email").value("email1")
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
