package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.ItemDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> getItems(Long userId);

    ItemDto getById(Long itemId);

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    Collection<ItemDto> search(String text);
}
