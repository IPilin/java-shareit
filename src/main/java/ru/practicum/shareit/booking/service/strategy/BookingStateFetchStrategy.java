package ru.practicum.shareit.booking.service.strategy;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingStateFetchStrategy {
    Sort SORT_BY_DESC = Sort.by(Sort.Direction.DESC, "start");

    List<Booking> findBookingList(User booker, Boolean owner, Integer from, Integer size);

    State getStrategyState();
}
