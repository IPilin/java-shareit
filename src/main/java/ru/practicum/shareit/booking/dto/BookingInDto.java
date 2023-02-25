package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.marker.OnCreate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingInDto {
    @NotNull(groups = OnCreate.class)
    Long itemId;
    @FutureOrPresent(groups = OnCreate.class)
    LocalDateTime start;
    @Future(groups = OnCreate.class)
    LocalDateTime end;
}
