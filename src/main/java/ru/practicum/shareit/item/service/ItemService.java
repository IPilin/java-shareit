package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentOutDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> getItems(Long userId);

    Item findById(Long itemId);

    Item findById(Long itemId, Long userId);

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    Collection<ItemDto> search(String text);

    CommentOutDto comment(Comment comment, Long itemId, Long userId);
}
