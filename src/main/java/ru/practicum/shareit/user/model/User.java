package ru.practicum.shareit.user.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.marker.OnCreate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    String name;
    String email;

    public User update(User user) {
        Optional.ofNullable(user.getName()).ifPresent((name) -> this.name = name);
        Optional.ofNullable(user.getEmail()).ifPresent((email) -> this.email = email);
        return this;
    }
}
