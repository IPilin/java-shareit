package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ValidationException("This email is already exists.");
        }
        return userRepository.create(user);
    }

    @Override
    public User updateUser(Long userId, User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ValidationException("This email is already exists.");
        }
        return userRepository.update(userId, user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.remove(userId);
    }
}
