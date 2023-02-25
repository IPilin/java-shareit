package ru.practicum.shareit.booking.service.strategy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.strategy.BookingStateFetchStrategy;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
public class BookingStateWaitingStrategy implements BookingStateFetchStrategy {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingStateWaitingStrategy(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Booking> findBookingList(User booker, Boolean owner, Integer from, Integer size) {
        if (owner) {
            return bookingRepository.findAllByItemOwnerIdAndStatusEquals(booker.getId(), BookingStatus.WAITING,
                    PageRequest.of(from / size, size, SORT_BY_DESC));
        }
        return bookingRepository.findAllByBookerAndStatusEquals(booker, BookingStatus.WAITING,
                PageRequest.of(from / size, size, SORT_BY_DESC));
    }

    @Override
    public State getStrategyState() {
        return State.WAITING;
    }
}
