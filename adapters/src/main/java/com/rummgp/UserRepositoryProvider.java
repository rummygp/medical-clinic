package com.rummgp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryProvider implements UserRepositoryPort {

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }
}
