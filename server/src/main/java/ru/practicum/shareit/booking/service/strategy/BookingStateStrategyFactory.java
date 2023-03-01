package ru.practicum.shareit.booking.service.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class BookingStateStrategyFactory {
    private final Map<State, BookingStateFetchStrategy> strategies;

    @Autowired
    public BookingStateStrategyFactory(Set<BookingStateFetchStrategy> strategiesSet) {
        strategies = new HashMap<>();
        strategiesSet.forEach(strategy -> strategies.put(strategy.getStrategyState(), strategy));
    }

    public BookingStateFetchStrategy findStrategy(State state) {
        return strategies.get(state);
    }
}
