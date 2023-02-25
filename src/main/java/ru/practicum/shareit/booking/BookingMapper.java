package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;
import java.util.stream.Collectors;

public class BookingMapper {
    public static Booking fromDto(BookingInDto bookingInDto) {
        return Booking.builder()
                .start(bookingInDto.getStart())
                .end(bookingInDto.getEnd())
                .build();
    }

    public static BookingOutDto toDto(Booking booking) {
        return BookingOutDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(new BookingOutDto.Booker(booking.getBooker().getId(), booking.getBooker().getName()))
                .bookerId(booking.getBooker().getId())
                .item(new BookingOutDto.Item(booking.getItem().getId(), booking.getItem().getName()))
                .build();
    }

    public static Collection<BookingOutDto> toDto(Collection<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }
}
