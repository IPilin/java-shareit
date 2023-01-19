package ru.practicum.shareit.user.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.marker.OnCreate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    @NotNull(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    String name;
    @NotNull(groups = OnCreate.class)
    @Email(groups = OnCreate.class)
    String email;

    public void update(User updatedUser) {
        Optional.ofNullable(updatedUser.getName()).ifPresent((name) -> this.name = name);
        Optional.ofNullable(updatedUser.getEmail()).ifPresent((email) -> this.email = email);
    }
}
