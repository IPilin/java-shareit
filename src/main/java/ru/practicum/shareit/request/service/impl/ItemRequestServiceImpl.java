package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestOutDto create(Long userId, ItemRequest itemRequest) {
        var user = userService.findById(userId);
        itemRequest.setRequester(user);
        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public Collection<ItemRequestOutDto> findAllByOwner(Long userId) {
        userService.findById(userId);
        var requests = itemRequestRepository.findAllByRequesterIdOrderByCreated(userId);
        loadItems(requests);
        return ItemRequestMapper.toDto(requests);
    }

    @Override
    public Collection<ItemRequestOutDto> findAll(Long userId, int from, int size) {
        userService.findById(userId);
        var sortByCreated = Sort.by(Sort.Direction.DESC, "created");
        var requests = itemRequestRepository.findAll(PageRequest.of(from / size, size, sortByCreated)).stream()
                        .filter(itemRequest -> !itemRequest.getRequester().getId().equals(userId))
                        .collect(Collectors.toList());
        loadItems(requests);
        return ItemRequestMapper.toDto(requests);
    }

    @Override
    public ItemRequestOutDto findById(Long userId, Long requestId) {
        userService.findById(userId);
        var request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("ItemRequest not found."));
        loadItems(List.of(request));
        return ItemRequestMapper.toDto(request);
    }

    private void loadItems(List<ItemRequest> requests) {
        Map<Long, List<ItemRequest>> requestById = requests.stream().collect(groupingBy(ItemRequest::getId));

        Map<Long, Set<Item>> items = itemRepository.findByRequesterIn(requestById.keySet())
                .stream()
                .collect(groupingBy(Item::getRequester, toSet()));

        requests.forEach(itemRequest -> itemRequest.setItems(items.getOrDefault(itemRequest.getId(), Collections.emptySet())));
    }
}
