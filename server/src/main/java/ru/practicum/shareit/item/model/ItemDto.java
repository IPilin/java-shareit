package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @EqualsAndHashCode.Include
    Long id;
    String name;
    String description;
    Boolean available;
    Long requestId;
    Set<CommentOutDto> comments;
    BookingOutDto lastBooking;
    BookingOutDto nextBooking;
}
