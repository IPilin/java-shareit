package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.marker.OnCreate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDto {
    @NotNull(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    String name;
    @NotNull(groups = OnCreate.class)
    @Email(groups = OnCreate.class)
    String email;
}