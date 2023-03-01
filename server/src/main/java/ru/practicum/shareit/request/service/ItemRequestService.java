package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestOutDto create(Long userId, ItemRequest itemRequest);

    Collection<ItemRequestOutDto> findAllByOwner(Long userId);

    Collection<ItemRequestOutDto> findAll(Long userId, int from, int size);

    ItemRequestOutDto findById(Long userId, Long requestId);
}
