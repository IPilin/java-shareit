package ru.practicum.shareit.user.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.marker.OnCreate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @EqualsAndHashCode.Include
    Long id;
    @NotNull(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    String name;
    @NotNull(groups = OnCreate.class)
    @Email(groups = OnCreate.class)
    String email;
}
