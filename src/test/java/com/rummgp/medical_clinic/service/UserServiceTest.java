package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.dto.UserDto;
import com.rummgp.medical_clinic.exception.FieldsShouldNotBeNullException;
import com.rummgp.medical_clinic.exception.NotFoundException;
import com.rummgp.medical_clinic.exception.UsernameAlreadyExistsException;
import com.rummgp.medical_clinic.mapper.PageMapper;
import com.rummgp.medical_clinic.mapper.UserMapper;
import com.rummgp.medical_clinic.model.User;
import com.rummgp.medical_clinic.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PageMapper pageMapper;
    private UserService userService;

    @BeforeEach
    void setup() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.pageMapper = Mappers.getMapper(PageMapper.class);
        this.userService = new UserService(userRepository, userMapper, pageMapper);
    }

    @Test
    void findAll_DataCorrect_PageUsersReturned() {
        //given
        User user1 = User.builder()
                .id(1L)
                .username("userUsername1")
                .email("userEmail1")
                .password("userPassword1")
                .build();
        User user2 = User.builder()
                .id(2L)
                .username("userUsername2")
                .email("userEmail2")
                .password("userPassword2")
                .build();
        List<User> users = List.of(user1, user2);
        Pageable pageable = PageRequest.of(0, 2);
        Page<User> page = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAll(pageable)).thenReturn(page);
        //when
        PageDto<UserDto> result = userService.findAll(pageable);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.content().get(0).id()),
                () -> assertEquals("userUsername1", result.content().get(0).username()),
                () -> assertEquals("userEmail1", result.content().get(0).email()),
                () -> assertEquals(2L, result.content().get(1).id()),
                () -> assertEquals("userUsername2", result.content().get(1).username()),
                () -> assertEquals("userEmail2", result.content().get(1).email())
        );

        verify(userRepository).findAll(pageable);
    }

    @Test
    void find_DataCorrect_UserReturned() {
        //given
        User user = User.builder()
                .id(1L)
                .username("userUsername")
                .email("userEmail")
                .password("userPassword")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        //when
        User result = userService.find(1L);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("userUsername", result.getUsername()),
                () -> assertEquals("userEmail", result.getEmail())
        );

        verify(userRepository).findById(1L);
    }

    @Test
    void find_UserNotFound_ExceptionThrown() {
        //given
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class, () -> userService.find(1L));
        //then
        Assertions.assertAll(
                () -> assertEquals("User with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );

        verify(userRepository).findById(userId);
    }

    @Test
    void add_DataCorrect_UserReturned() {
        //given
        User user = User.builder()
                .id(1L)
                .username("userUsername")
                .email("userEmail")
                .password("userPassword")
                .build();

        when(userRepository.save(user)).thenReturn(user);
        //when
        User result = userService.add(user);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("userUsername", result.getUsername()),
                () -> assertEquals("userEmail", result.getEmail()),
                () -> assertEquals("userPassword", result.getPassword())
        );
    }

    @Test
    void add_UserFieldsShouldNotBeNull_ExceptionThrown() {
        //given
        User user = User.builder()
                .username(null)
                .email("userEmail")
                .password("userPassword")
                .build();
        //when
        FieldsShouldNotBeNullException exception = Assertions.assertThrowsExactly(FieldsShouldNotBeNullException.class,
                () -> userService.add(user));
        //then
        Assertions.assertAll(
                () -> assertEquals("Fields should not be null", exception.getMessage()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus())
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void add_UsernameAlreadyExists_ExceptionThrown() {
        //given
        User user = User.builder()
                .username("existingUsername")
                .email("userEmail")
                .password("userPassword")
                .build();

        when(userRepository.findByUsername("existingUsername")).thenReturn(Optional.of(user));
        //when
        UsernameAlreadyExistsException exception = Assertions.assertThrowsExactly(UsernameAlreadyExistsException.class,
                () -> userService.add(user));
        //then
        Assertions.assertAll(
                () -> assertEquals("Username existingUsername is already taken", exception.getMessage()),
                () -> assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus())
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_DataCorrect_UserReturned() {
        //given
        String newPassword = "newPassword";
        User user = User.builder()
                .id(1L)
                .username("userUsername")
                .email("userEmail")
                .password("userPassword")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        //when
        User result = userService.changePassword(1L, newPassword);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("userUsername", result.getUsername()),
                () -> assertEquals("userEmail", result.getEmail()),
                () -> assertEquals("newPassword", result.getPassword())
        );

        verify(userRepository).findById(any());
    }

    @Test
    void changePassword_FieldsShouldNotBeNullException_ExceptionThrown() {
        //given
        String password = null;
        Long userId = 1L;
        //when
        FieldsShouldNotBeNullException exception = Assertions.assertThrowsExactly(FieldsShouldNotBeNullException.class,
                () -> userService.changePassword(userId, password));
        //then
        Assertions.assertAll(
                () -> assertEquals("Fields should not be null", exception.getMessage()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus())
        );

        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_UserNotFound_ExceptionThrown() {
        //given
        String newPassword = "newPassword";
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> userService.changePassword(userId, newPassword));
        //then
        Assertions.assertAll(
                () -> assertEquals("User with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus())
        );

        verify(userRepository).findById(any());
        verify(userRepository, never()).save(any());
    }
}
