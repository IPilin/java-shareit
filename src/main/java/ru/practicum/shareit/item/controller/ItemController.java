package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.CommentInDto;
import ru.practicum.shareit.item.model.CommentOutDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.marker.OnCreate;
import ru.practicum.shareit.marker.OnUpdate;


import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable("itemId") Long itemId,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ItemMapper.toDto(itemService.findById(itemId, userId));
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody @Validated(OnCreate.class) ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable("itemId") Long itemId,
                              @RequestBody @Validated(OnUpdate.class) ItemDto itemDto) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam("text") String text) {
        return itemService.search(text);
    }

    @PostMapping("{itemId}/comment")
    public CommentOutDto commented(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                                   @Validated({OnCreate.class}) @RequestBody CommentInDto commentInDto) {
        return itemService.comment(CommentMapper.fromDto(commentInDto), itemId, userId);
    }
}
