package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.marker.OnCreate;
import ru.practicum.shareit.marker.OnUpdate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(value = "size", defaultValue = "5") @Positive int size) {
        return itemClient.getAll(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable("itemId") Long itemId,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getById(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody @Validated(OnCreate.class) ItemRequestDto itemDto) {
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable("itemId") Long itemId,
                              @RequestBody @Validated(OnUpdate.class) ItemRequestDto itemDto) {
        return itemClient.update(itemDto, itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam("text") String text,
                                           @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(value = "size", defaultValue = "5") @Positive int size) {
        return itemClient.search(text, from, size);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> commented(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                                   @Validated({OnCreate.class}) @RequestBody CommentRequestDto commentInDto) {
        return itemClient.commented(itemId, userId, commentInDto);
    }
}
