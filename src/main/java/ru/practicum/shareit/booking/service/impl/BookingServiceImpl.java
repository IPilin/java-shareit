package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    static Sort SORT_BY_DESC = Sort.by(Sort.Direction.DESC, "start");

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public BookingOutDto create(Long userId, BookingInDto bookingDto) {
        var user = userService.findById(userId);
        var item = itemService.findById(bookingDto.getItemId());

        if (!item.getAvailable()) {
            throw new ValidationException("Item isn't available.");
        }
        if (item.getOwnerId().equals(userId)) {
            throw new NotFoundException("Refused access.");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException("Wrong end date.");
        }

        var booking = BookingMapper.fromDto(bookingDto);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingOutDto approve(Long userId, Long bookingId, Boolean approve) {
        var booking = findById(userId, bookingId);
        userService.findById(userId);

        if (!booking.getItem().getOwnerId().equals(userId)) {
            throw new NotFoundException("Refused access.");
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException(String.format("Booking has %s already.", booking.getStatus()));
        }

        if (approve) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public Booking findById(Long userId, Long bookingId) {
        var booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found."));
        userService.findById(userId);
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwnerId().equals(userId)) {
            throw new NotFoundException("Refused access. User or Owner don't match.");
        }
        return booking;
    }

    @Override
    public BookingOutDto findByIdDto(Long userId, Long bookingId) {
        return BookingMapper.toDto(findById(userId, bookingId));
    }

    @Override
    public Collection<BookingOutDto> findAll(Long userId, State state) {
        var booker = userService.findById(userId);
        List<Booking> bookings = List.of();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBooker(booker, SORT_BY_DESC);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerAndEndBefore(booker, LocalDateTime.now(), SORT_BY_DESC);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerAndStartAfter(booker, LocalDateTime.now(), SORT_BY_DESC);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(booker, LocalDateTime.now(),
                        LocalDateTime.now(), SORT_BY_DESC);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerAndStatusEquals(booker, BookingStatus.WAITING, SORT_BY_DESC);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerAndStatusEquals(booker, BookingStatus.REJECTED, SORT_BY_DESC);
                break;
        }
        return BookingMapper.toDto(bookings);
    }

    @Override
    public Collection<BookingOutDto> findAllOwner(Long userId, State state) {
        userService.findById(userId);
        List<Booking> bookings = List.of();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemOwnerId(userId, SORT_BY_DESC);
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemOwnerIdAndEndBefore(userId, LocalDateTime.now(), SORT_BY_DESC);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartAfter(userId, LocalDateTime.now(), SORT_BY_DESC);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now(), SORT_BY_DESC);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusEquals(userId, BookingStatus.WAITING, SORT_BY_DESC);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusEquals(userId, BookingStatus.REJECTED, SORT_BY_DESC);
                break;
        }
        return BookingMapper.toDto(bookings);
    }
}
