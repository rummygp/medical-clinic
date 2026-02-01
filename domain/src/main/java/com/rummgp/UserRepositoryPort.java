package com.rummgp;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findById(Long id);
}
