package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

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
    public UserDto getById(Long userId) {
        return UserMapper.toDto(userRepository.findById(userId));
    }

    @Override
    public UserDto create(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ValidationException("This email is already exists.");
        }
        return UserMapper.toDto(userRepository.create(UserMapper.fromDto(user)));
    }

    @Override
    public UserDto update(Long userId, UserDto user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ValidationException("This email is already exists.");
        }
        return UserMapper.toDto(userRepository.update(userId, UserMapper.fromDto(user)));
    }

    @Override
    public void delete(Long userId) {
        userRepository.remove(userId);
    }
}
