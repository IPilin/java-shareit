package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class UserTest {
    @Test
    public void updateUserTest() {
        var user = new User(1L, "name", "email");
        var updatedUser = new User(1L, "updated name", "updated email");

        user.update(updatedUser);

        assertEquals("User name doesn't updated", user.getName(), updatedUser.getName());
        assertEquals("User email doesn't updated", user.getEmail(), updatedUser.getEmail());
    }
}
