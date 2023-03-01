package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.Collection;
import java.util.stream.Collectors;

public class ItemMapper {
    public static Item fromDto(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .requester(itemDto.getRequestId())
                .build();
    }

    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequester())
                .comments(item.getComments() == null ? null : CommentMapper.toDto(item.getComments()))
                .lastBooking(item.getLastBooking() == null ? null : BookingMapper.toDto(item.getLastBooking()))
                .nextBooking(item.getNextBooking() == null ? null : BookingMapper.toDto(item.getNextBooking()))
                .build();
    }

    public static Collection<ItemDto> toDto(Collection<Item> items) {
        return items.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
