package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.marker.OnCreate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody @Validated(OnCreate.class) BookItemRequestDto bookingDto) {
        return bookingClient.bookItem(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable("bookingId") Long bookingId,
                                                 @RequestParam("approved") Boolean approved) {
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable("bookingId") Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(value = "state", required = false, defaultValue = "ALL") BookingState state,
                                          @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(value = "size", defaultValue = "5") @Positive int size) {
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                               @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(value = "size", defaultValue = "5") @Positive int size) {
        return bookingClient.getBookingsOwner(userId, state, from, size);
    }
}
