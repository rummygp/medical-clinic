package com.rummgp;

import com.rummgp.exception.FieldsShouldNotBeNullException;
import com.rummgp.exception.UsernameAlreadyExistsException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserValidator {

    public static void validateUserCreate(User user, UserRepositoryPort userRepositoryPort) {
        if (user.getId() == null &&
                (user.getEmail() == null ||
                        user.getUsername() == null ||
                        user.getPassword() == null)) {
            throw new FieldsShouldNotBeNullException();
        }
        if (user.getId() == null && userRepositoryPort.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
    }

    public static void validatePasswordEdit(String password) {
        if (password == null) {
            throw new FieldsShouldNotBeNullException();
        }
    }
}
