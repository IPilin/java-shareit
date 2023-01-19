package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();
    UserDto getUserById(Long userId);
    UserDto createUser(UserDto user);
    UserDto updateUser(Long userId, UserDto user);
    void deleteUser(Long userId);
}
