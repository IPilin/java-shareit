package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.exception.model.WrongAccessException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentOutDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final CommentStorage commentStorage;
    private final UserService userService;
    private final BookingRepository bookingRepository;

    @Override
    public Collection<ItemDto> getItems(Long userId) {
        var items = itemStorage.findAllByOwnerIdOrderById(userId);
        items.forEach(this::setBookingsToItem);
        return ItemMapper.toDto(items);
    }

    @Override
    public Item findById(Long itemId) {
        return itemStorage.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found."));
    }

    @Override
    public Item findById(Long itemId, Long userId) {
        var item = findById(itemId);
        userService.findById(userId);
        loadComments(List.of(item));
        if (item.getOwnerId().equals(userId)) {
            setBookingsToItem(item);
        }
        return item;
    }

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        var item = ItemMapper.fromDto(itemDto);
        item.setOwnerId(userService.findById(userId).getId());
        return ItemMapper.toDto(itemStorage.save(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        if (!Objects.equals(findById(itemId).getOwnerId(), userId)) {
            throw new WrongAccessException("You not the owner of that item.");
        }

        var updatedItem = findById(itemId);
        updatedItem.update(ItemMapper.fromDto(itemDto));

        return ItemMapper.toDto(itemStorage.save(updatedItem));
    }

    @Override
    public Collection<ItemDto> search(String text) {
        return text.isEmpty() ? new ArrayList<>() : ItemMapper.toDto(itemStorage.search(text));
    }

    @Transactional
    @Override
    public CommentOutDto comment(Comment comment, Long itemId, Long userId) {
        var user = userService.findById(userId);
        var item = findById(itemId);

        if (bookingRepository.findAllByBookerAndItemAndStatusEqualsAndEndBefore(user, item, BookingStatus.APPROVED,
                LocalDateTime.now()).isEmpty()) {
            throw new ValidationException("Refused access to add comment.");
        }
        comment.setItem(item);
        comment.setAuthor(user);

        return CommentMapper.toDto(commentStorage.save(comment));
    }

    private void setBookingsToItem(Item item) {
        Optional<Booking> lastBooking = bookingRepository.findLastBookingByItemId(item.getId(), LocalDateTime.now());
        item.setLastBooking(lastBooking.orElse(null));
        Optional<Booking> nextBooking = bookingRepository.findNextBookingByItemId(item.getId(), LocalDateTime.now());
        item.setNextBooking(nextBooking.orElse(null));
    }

    private void loadComments(List<Item> items) {
        Map<Item, Set<Comment>> comments = commentStorage.findByItemIn(items)
                .stream()
                .collect(groupingBy(Comment::getItem, toSet()));

        items.forEach(item -> item.setComments(comments.getOrDefault(item, Collections.emptySet())));
    }
}
