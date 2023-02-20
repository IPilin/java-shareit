package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> getAll() {
        return UserMapper.toDto(userRepository.findAll());
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));
    }

    @Transactional
    @Override
    public UserDto create(UserDto user) {
        return UserMapper.toDto(userRepository.save(UserMapper.fromDto(user)));
    }

    @Override
    public UserDto update(Long userId, UserDto user) {
        var updatedUser = findById(userId);
        updatedUser.update(UserMapper.fromDto(user));
        return UserMapper.toDto(userRepository.save(updatedUser));
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}
