package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.Collection;

public interface BookingService {
    BookingOutDto create(Long userId, BookingInDto bookingDto);

    BookingOutDto approve(Long userId, Long bookingId, Boolean approve);

    Booking findById(Long userId, Long bookingId);

    BookingOutDto findByIdDto(Long userId, Long bookingId);

    Collection<BookingOutDto> findAll(Long userId, State state);

    Collection<BookingOutDto> findAllOwner(Long userId, State state);
}
