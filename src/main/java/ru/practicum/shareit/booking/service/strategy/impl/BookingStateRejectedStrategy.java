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
    public List<Booking> findBookingList(User booker, Boolean owner, Integer from, Integer size) {
        if (owner) {
            return bookingRepository.findAllByItemOwnerIdAndStatusEquals(booker.getId(), BookingStatus.REJECTED,
                    getRequest(from, size));
        }
        return bookingRepository.findAllByBookerAndStatusEquals(booker, BookingStatus.REJECTED,
                getRequest(from, size));
    }

    @Override
    public State getStrategyState() {
        return State.REJECTED;
    }
}
