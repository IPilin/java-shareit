package ru.practicum.shareit.booking.service.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
public class BookingStateCurrentStrategy implements BookingStateFetchStrategy {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> findBookingList(User booker, Boolean owner, Integer from, Integer size) {
        if (owner) {
            return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(booker.getId(), LocalDateTime.now(),
                    LocalDateTime.now(), PageRequest.of(from / size, size, SORT_BY_DESC));
        }
        return bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(booker, LocalDateTime.now(),
                LocalDateTime.now(), PageRequest.of(from / size, size, SORT_BY_DESC));
    }

    @Override
    public State getStrategyState() {
        return State.CURRENT;
    }
}
