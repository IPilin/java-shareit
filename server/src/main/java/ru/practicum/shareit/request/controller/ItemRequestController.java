package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestOutDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestBody ItemRequestInDto itemRequest) {
        return itemRequestService.create(userId, ItemRequestMapper.fromDto(itemRequest));
    }

    @GetMapping
    public Collection<ItemRequestOutDto> getAllByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.findAllByOwner(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestOutDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                @RequestParam(value = "size", defaultValue = "5") Integer to) {
        return itemRequestService.findAll(userId, from, to);
    }

    @GetMapping("{requestId}")
    public ItemRequestOutDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable("requestId") Long requestId) {
        return itemRequestService.findById(userId, requestId);
    }
}
