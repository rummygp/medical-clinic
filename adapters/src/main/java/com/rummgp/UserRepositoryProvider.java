package com.rummgp;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryProvider implements UserRepositoryPort {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PageMapper pageMapper;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toPojo);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toPojo);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        return userMapper.toPojo(userRepository.save(userEntity));
    }

    @Override
    public PagePojo<User> findAll(UserFindCommand userFindCommand) {
        Pageable pageable = PageRequest.of(userFindCommand.pageNumber(), userFindCommand.pageSize());
        Page<UserEntity> page = userRepository.findAll(pageable);
        return pageMapper.toPojo(page, userMapper::toPojo);
    }
}
