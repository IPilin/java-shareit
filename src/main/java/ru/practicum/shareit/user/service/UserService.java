package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAll();

    User findById(Long userId);

    UserDto create(UserDto user);

    UserDto update(Long userId, UserDto user);

    void delete(Long userId);
}
