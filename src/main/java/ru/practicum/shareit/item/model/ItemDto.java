package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.marker.OnCreate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @EqualsAndHashCode.Include
    Long id;
    @NotNull(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    String name;
    @NotNull(groups = OnCreate.class)
    @NotBlank(groups = OnCreate.class)
    String description;
    @NotNull(groups = OnCreate.class)
    Boolean available;
    Set<CommentOutDto> comments;
    BookingOutDto lastBooking;
    BookingOutDto nextBooking;
}
