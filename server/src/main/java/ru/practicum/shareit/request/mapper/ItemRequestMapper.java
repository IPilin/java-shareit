package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemRequest fromDto(ItemRequestInDto requestInDto) {
        return ItemRequest.builder()
                .description(requestInDto.getDescription())
                .created(LocalDateTime.now())
                .build();
    }

    public static ItemRequestOutDto toDto(ItemRequest itemRequest) {
        return ItemRequestOutDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemRequest.getItems() == null ? null : ItemMapper.toDto(itemRequest.getItems()))
                .build();
    }

    public static Collection<ItemRequestOutDto> toDto(Collection<ItemRequest> itemRequests) {
        return itemRequests.stream()
                .map(ItemRequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
