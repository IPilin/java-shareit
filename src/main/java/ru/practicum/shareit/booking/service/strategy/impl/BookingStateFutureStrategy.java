package ru.practicum.shareit.booking.service.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.strategy.BookingStateFetchStrategy;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingStateFutureStrategy implements BookingStateFetchStrategy {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> findBookingList(User booker, Boolean owner) {
        if (owner) {
            return bookingRepository.findAllByItemOwnerIdAndStartAfter(booker.getId(), LocalDateTime.now(), SORT_BY_DESC);
        }
        return bookingRepository.findAllByBookerAndStartAfter(booker, LocalDateTime.now(), SORT_BY_DESC);
    }

    @Override
    public State getStrategyState() {
        return State.FUTURE;
    }
}
