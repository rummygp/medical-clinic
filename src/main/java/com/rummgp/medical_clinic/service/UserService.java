package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.exception.notFound.UserNotFoundException;
import com.rummgp.medical_clinic.model.User;
import com.rummgp.medical_clinic.repository.UserRepository;
import com.rummgp.medical_clinic.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User find(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User add(User user) {
        UserValidator.validateUserCreate(user, userRepository);
        return userRepository.save(user);
    }

    public User changePassword(Long id, String password) {
        UserValidator.validatePasswordEdit(password);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setPassword(password);
        return userRepository.save(user);
    }
}
