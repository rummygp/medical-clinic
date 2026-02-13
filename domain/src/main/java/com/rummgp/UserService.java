package com.rummgp;

import com.rummgp.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryPort userRepositoryPort;

    public PagePojo<User> findAll(UserFindCommand userFindCommand) {
        return userRepositoryPort.findAll(userFindCommand);
    }

    public User find(Long id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
    }

    public User add(User user) {
        UserValidator.validateUserCreate(user, userRepositoryPort);
        return userRepositoryPort.save(user);
    }

    public User changePassword(Long id, String password) {
        UserValidator.validatePasswordEdit(password);
        User user = userRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id));
        user.setPassword(password);
        return userRepositoryPort.save(user);
    }
}
