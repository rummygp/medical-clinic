package com.rummgp.medical_clinic.validator;

import com.rummgp.medical_clinic.exception.UsernameAlreadyExistsException;
import com.rummgp.medical_clinic.model.User;
import com.rummgp.medical_clinic.repository.UserRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserValidator {

    public static void validateUserCreate(User user, UserRepository userRepository) {
        if (user.getId() == null &&
                (user.getEmail() == null ||
                        user.getUsername() == null ||
                        user.getPassword() == null)) {
            throw new IllegalArgumentException("Fields should not be null");
        }
        if (user.getId() == null && userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
    }

    public static void validatePasswordEdit(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Fields should not be null");
        }
    }
}
