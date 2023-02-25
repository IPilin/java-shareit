package ru.practicum.shareit.booking.service.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.strategy.BookingStateFetchStrategy;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingStateAllStrategy implements BookingStateFetchStrategy {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> findBookingList(User booker, Boolean owner) {
        if (owner) {
            return bookingRepository.findAllByItemOwnerId(booker.getId(), SORT_BY_DESC);
        }
        return bookingRepository.findAllByBooker(booker, SORT_BY_DESC);
    }

    @Override
    public State getStrategyState() {
        return State.ALL;
    }
}
