package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.service.strategy.BookingStateStrategyFactory;
import ru.practicum.shareit.booking.service.strategy.impl.*;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    BookingStateStrategyFactory factory;
    @Mock
    BookingStateAllStrategy allStrategy;
    @Mock
    BookingStateCurrentStrategy currentStrategy;
    @Mock
    BookingStateFutureStrategy futureStrategy;
    @Mock
    BookingStatePastStrategy pastStrategy;
    @Mock
    BookingStateRejectedStrategy rejectedStrategy;
    @Mock
    BookingStateWaitingStrategy waitingStrategy;
    BookingService service;
    @Mock
    BookingRepository repository;
    @Mock
    ItemServiceImpl itemService;
    @Mock
    UserServiceImpl userService;
    User user;
    Item item;
    BookingInDto bookingShortDto;
    Booking booking;

    @BeforeEach
    public void setup() {
        user = new User(2L, "userName", "user@email.ru");
        item = new Item(1L, "itemName", "itemDescription", true, 1L, null, null, null, null);
        bookingShortDto = new BookingInDto(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(1));
        booking = new Booking(1L, bookingShortDto.getStart(), bookingShortDto.getEnd(), item, user, BookingStatus.WAITING);

        allStrategy = new BookingStateAllStrategy(repository);
        currentStrategy = new BookingStateCurrentStrategy(repository);
        futureStrategy = new BookingStateFutureStrategy(repository);
        pastStrategy = new BookingStatePastStrategy(repository);
        rejectedStrategy = new BookingStateRejectedStrategy(repository);
        waitingStrategy = new BookingStateWaitingStrategy(repository);

        factory = new BookingStateStrategyFactory(new HashSet<>(Arrays.asList(
                allStrategy, currentStrategy, futureStrategy, pastStrategy, rejectedStrategy, waitingStrategy
        )));
        service = new BookingServiceImpl(repository, itemService, userService, factory);
    }

    @Test
    void save() {
        when(itemService.findById(anyLong()))
                .thenReturn(item);
        when(userService.findById(anyLong()))
                .thenReturn(user);
        when(repository.save(any()))
                .thenReturn(booking);

        BookingOutDto savedBookingDto = service.create(2L, bookingShortDto);

        assertThat(savedBookingDto.getId()).isEqualTo(1L);
        assertThat(savedBookingDto.getStart()).isNotNull();
        assertThat(savedBookingDto.getEnd()).isNotNull();
        assertThat(savedBookingDto.getStatus()).isEqualTo(BookingStatus.WAITING);
        assertThat(savedBookingDto.getBooker().getId()).isEqualTo(2L);
        assertThat(savedBookingDto.getItem().getId()).isEqualTo(item.getId());

        assertThrows(NotFoundException.class, () -> service.create(1L, bookingShortDto));

        item.setAvailable(false);
        assertThrows(ValidationException.class, () -> service.create(2L, bookingShortDto));

        bookingShortDto.setEnd(LocalDateTime.now());
        assertThrows(ValidationException.class, () -> service.create(2L, bookingShortDto));
    }


    @Test
    void approve() {
        when(userService.findById(anyLong()))
                .thenReturn(user);
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        when(repository.save(any()))
                .thenReturn(booking);

        BookingOutDto approvedBookingDto = service.approve(1L, booking.getId(), true);

        assertThat(approvedBookingDto.getStatus()).isEqualTo(BookingStatus.APPROVED);

        assertThrows(NotFoundException.class, () -> service.approve(2L, booking.getId(), true));

        booking.setStatus(BookingStatus.APPROVED);
        assertThrows(ValidationException.class, () -> service.approve(1L, booking.getId(), true));
    }

    @Test
    void findById() {
        when(userService.findById(anyLong()))
                .thenReturn(user);
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(booking));

        Booking foundBookingDto = service.findById(1L, booking.getId());

        assertThat(foundBookingDto.getId()).isEqualTo(1L);
        assertThat(foundBookingDto.getStart()).isNotNull();
        assertThat(foundBookingDto.getEnd()).isNotNull();
        assertThat(foundBookingDto.getStatus()).isEqualTo(BookingStatus.WAITING);
        assertThat(foundBookingDto.getBooker().getId()).isEqualTo(2L);
        assertThat(foundBookingDto.getItem().getId()).isEqualTo(item.getId());

        assertThrows(NotFoundException.class, () -> service.findById(3L, 4L));
    }

    @Test
    void findAllByBookerId() {
        when(userService.findById(anyLong()))
                .thenReturn(user);

        var bookings = List.of(booking);

        when(repository.findAllByBooker(any(), any()))
                .thenReturn(bookings);
        when(repository.findAllByBookerAndStartBeforeAndEndAfter(any(), any(), any(), any()))
                .thenReturn(bookings);
        when(repository.findAllByBookerAndEndBefore(any(), any(), any()))
                .thenReturn(bookings);
        when(repository.findAllByBookerAndStartAfter(any(), any(), any()))
                .thenReturn(bookings);
        when(repository.findAllByBookerAndStatusEquals(any(), any(), any()))
                .thenReturn(bookings);


        assertThat(service.findAll(booking.getBooker().getId(), State.ALL, 0, 20).size()).isEqualTo(1);
        assertThat(service.findAll(booking.getBooker().getId(), State.CURRENT, 0, 20).size()).isEqualTo(1);
        assertThat(service.findAll(booking.getBooker().getId(), State.PAST, 0, 20).size()).isEqualTo(1);
        assertThat(service.findAll(booking.getBooker().getId(), State.FUTURE, 0, 20).size()).isEqualTo(1);
        assertThat(service.findAll(booking.getBooker().getId(), State.WAITING, 0, 20).size()).isEqualTo(1);
    }

    @Test
    void findAllByOwnerId() {
        when(userService.findById(anyLong()))
                .thenReturn(user);

        var bookings = List.of(booking);

        when(repository.findAllByItemOwnerId(anyLong(), any()))
                .thenReturn(bookings);
        when(repository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(anyLong(), any(), any(), any()))
                .thenReturn(bookings);
        when(repository.findAllByItemOwnerIdAndEndBefore(anyLong(), any(), any()))
                .thenReturn(bookings);
        when(repository.findAllByItemOwnerIdAndStartAfter(anyLong(), any(), any()))
                .thenReturn(bookings);
        when(repository.findAllByItemOwnerIdAndStatusEquals(anyLong(), any(), any()))
                .thenReturn(bookings);

        assertThat(service.findAllOwner(booking.getItem().getOwnerId(), State.ALL, 0, 20).size()).isEqualTo(1);
        assertThat(service.findAllOwner(booking.getItem().getOwnerId(), State.CURRENT, 0, 20).size()).isEqualTo(1);
        assertThat(service.findAllOwner(booking.getItem().getOwnerId(), State.PAST, 0, 20).size()).isEqualTo(1);
        assertThat(service.findAllOwner(booking.getItem().getOwnerId(), State.FUTURE, 0, 20).size()).isEqualTo(1);
        assertThat(service.findAllOwner(booking.getItem().getOwnerId(), State.WAITING, 0, 20).size()).isEqualTo(1);
    }
}
