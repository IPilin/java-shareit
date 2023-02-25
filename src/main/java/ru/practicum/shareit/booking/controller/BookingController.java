package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.marker.OnCreate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;


@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingOutDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestBody @Validated(OnCreate.class) BookingInDto bookingDto) {
        return service.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable("bookingId") Long bookingId,
                                        @RequestParam("approved") Boolean approved) {
        return service.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingOutDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("bookingId") Long bookingId) {
        return service.findByIdDto(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingOutDto> findAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(required = false, defaultValue = "ALL") State state,
                                             @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(value = "size", defaultValue = "5") @Positive int size) {
        return service.findAll(userId, state, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingOutDto> findAllOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(required = false, defaultValue = "ALL") State state,
                                                  @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(value = "size", defaultValue = "5") @Positive int size) {
        return service.findAllOwner(userId, state, from, size);
    }
}
