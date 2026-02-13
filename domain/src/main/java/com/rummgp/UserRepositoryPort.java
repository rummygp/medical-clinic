package com.rummgp;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findById(Long id);

    PagePojo<User> findAll(UserFindCommand userFindCommand);

    User save(User user);

    Optional<User> findByUsername(String username);
}
