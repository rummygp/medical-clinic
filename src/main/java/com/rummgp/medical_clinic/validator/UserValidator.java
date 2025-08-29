package com.rummgp.medical_clinic.validator;

import com.rummgp.medical_clinic.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserValidator {

    public static void validateUserCreate(User user) {
        if (user.getUsername() == null ||
        user.getPassword() == null) {
            throw new IllegalArgumentException("Fields should not be null");
        }
    }

    public static void validatePasswordEdit(String password){
        if (password == null) {
            throw new IllegalArgumentException("Fields should not be null");
        }
    }
}
