package com.rummgp.service;

import com.rummgp.*;
import com.rummgp.exception.FieldsShouldNotBeNullException;
import com.rummgp.exception.NotFoundException;
import com.rummgp.exception.UsernameAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserRepositoryPort userRepositoryPort;;
    private UserService userService;

    @BeforeEach
    void setup() {
        this.userRepositoryPort = Mockito.mock(UserRepositoryPort.class);
        this.userService = new UserService(userRepositoryPort);
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
        UserFindCommand userFindCommand = UserFindCommand.builder()
                .pageNumber(0)
                .pageSize(20)
                .build();
        PagePojo<User> usersPage = new PagePojo<>(users, 0, 20, 1L, 1);

        when(userRepositoryPort.findAll(userFindCommand)).thenReturn(usersPage);
        //when
        PagePojo<User> result = userService.findAll(userFindCommand);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.content().get(0).getId()),
                () -> assertEquals("userUsername1", result.content().get(0).getUsername()),
                () -> assertEquals("userEmail1", result.content().get(0).getEmail()),
                () -> assertEquals(2L, result.content().get(1).getId()),
                () -> assertEquals("userUsername2", result.content().get(1).getUsername()),
                () -> assertEquals("userEmail2", result.content().get(1).getEmail())
        );

        verify(userRepositoryPort).findAll(userFindCommand);
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

        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        //when
        User result = userService.find(1L);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("userUsername", result.getUsername()),
                () -> assertEquals("userEmail", result.getEmail())
        );

        verify(userRepositoryPort).findById(1L);
    }

    @Test
    void find_UserNotFound_ExceptionThrown() {
        //given
        Long userId = 1L;

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class, () -> userService.find(1L));
        //then
        Assertions.assertAll(
                () -> assertEquals("User with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
        );

        verify(userRepositoryPort).findById(userId);
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

        when(userRepositoryPort.save(user)).thenReturn(user);
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
                () -> assertEquals(400, exception.getStatus())
        );

        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    void add_UsernameAlreadyExists_ExceptionThrown() {
        //given
        User user = User.builder()
                .username("existingUsername")
                .email("userEmail")
                .password("userPassword")
                .build();

        when(userRepositoryPort.findByUsername("existingUsername")).thenReturn(Optional.of(user));
        //when
        UsernameAlreadyExistsException exception = Assertions.assertThrowsExactly(UsernameAlreadyExistsException.class,
                () -> userService.add(user));
        //then
        Assertions.assertAll(
                () -> assertEquals("Username existingUsername is already taken", exception.getMessage()),
                () -> assertEquals(409, exception.getStatus())
        );

        verify(userRepositoryPort, never()).save(any());
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

        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(user));
        when(userRepositoryPort.save(user)).thenReturn(user);
        //when
        User result = userService.changePassword(1L, newPassword);
        //then
        Assertions.assertAll(
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals("userUsername", result.getUsername()),
                () -> assertEquals("userEmail", result.getEmail()),
                () -> assertEquals("newPassword", result.getPassword())
        );

        verify(userRepositoryPort).findById(any());
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
                () -> assertEquals(400, exception.getStatus())
        );

        verify(userRepositoryPort, never()).findById(any());
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    void changePassword_UserNotFound_ExceptionThrown() {
        //given
        String newPassword = "newPassword";
        Long userId = 1L;

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());
        //when
        NotFoundException exception = Assertions.assertThrowsExactly(NotFoundException.class,
                () -> userService.changePassword(userId, newPassword));
        //then
        Assertions.assertAll(
                () -> assertEquals("User with id: 1 doesn't exist", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus())
        );

        verify(userRepositoryPort).findById(any());
        verify(userRepositoryPort, never()).save(any());
    }
}
