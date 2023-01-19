package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.ItemDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> getAllUserItems(Long userId);
    ItemDto getItemById(Long itemId);
    ItemDto createItem(Long userId, ItemDto itemDto);
    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);
}
