package ru.practicum.shareit.booking.service.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.strategy.BookingStateFetchStrategy;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingStateRejectedStrategy implements BookingStateFetchStrategy {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> findBookingList(User booker, Boolean owner) {
        if (owner) {
            return bookingRepository.findAllByItemOwnerIdAndStatusEquals(booker.getId(), BookingStatus.REJECTED, SORT_BY_DESC);
        }
        return bookingRepository.findAllByBookerAndStatusEquals(booker, BookingStatus.REJECTED, SORT_BY_DESC);
    }

    @Override
    public State getStrategyState() {
        return State.REJECTED;
    }
}
