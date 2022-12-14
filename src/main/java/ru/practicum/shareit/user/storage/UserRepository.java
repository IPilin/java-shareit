package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> findAll();
    User findById(Long userId);
    User create(User user);
    User update(Long userId, User user);
    void remove(Long userId);
}
