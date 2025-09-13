package com.rummgp.medical_clinic.service;

import com.rummgp.medical_clinic.dto.PageDto;
import com.rummgp.medical_clinic.dto.UserDto;
import com.rummgp.medical_clinic.exception.NotFoundException;
import com.rummgp.medical_clinic.mapper.PageMapper;
import com.rummgp.medical_clinic.mapper.UserMapper;
import com.rummgp.medical_clinic.model.User;
import com.rummgp.medical_clinic.repository.UserRepository;
import com.rummgp.medical_clinic.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PageMapper pageMapper;

    public PageDto<UserDto> findAll(Pageable pageable) {
        return pageMapper.toDto(userRepository.findAll(pageable), userMapper::toDto);
    }

    public User find(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
    }

    public User add(User user) {
        UserValidator.validateUserCreate(user, userRepository);
        return userRepository.save(user);
    }

    public User changePassword(Long id, String password) {
        UserValidator.validatePasswordEdit(password);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        user.setPassword(password);
        return userRepository.save(user);
    }
}
