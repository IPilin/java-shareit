package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.model.ItemDto;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequestOutDto {
    @EqualsAndHashCode.Include
    Long id;
    String description;
    LocalDateTime created;
    Set<ItemDto> items;
}
