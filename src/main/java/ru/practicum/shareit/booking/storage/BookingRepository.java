package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker(User booker, Sort sort);

    List<Booking> findAllByBookerAndEndBefore(User booker, LocalDateTime time, Sort sort);

    List<Booking> findAllByBookerAndStartAfter(User booker, LocalDateTime time, Sort sort);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfter(User booker, LocalDateTime time, LocalDateTime timeNow, Sort sort);

    List<Booking> findAllByBookerAndStatusEquals(User booker, BookingStatus status, Sort sort);

    List<Booking> findAllByItemOwnerId(Long owner, Sort sort);

    List<Booking> findAllByItemOwnerIdAndEndBefore(Long owner, LocalDateTime time, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartAfter(Long owner, LocalDateTime time, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long owner, LocalDateTime time, LocalDateTime timeNow, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatusEquals(Long owner, BookingStatus status, Sort sort);

    @Query(value = "select * " +
            "from bookings b " +
            "left join items i on b.item_id = i.id " +
            "where i.id = ?1 " +
            "and b.end_date < ?2 " +
            "order by b.end_date desc " +
            "limit 1",
            nativeQuery = true)
    Optional<Booking> findLastBookingByItemId(Long itemId, LocalDateTime dateTime);

    @Query(value = "select * " +
            "from bookings b " +
            "left join items i on b.item_id = i.id " +
            "where i.id = ?1 " +
            "and b.start_date > ?2 " +
            "order by b.start_date " +
            "limit 1",
            nativeQuery = true)
    Optional<Booking> findNextBookingByItemId(Long itemId, LocalDateTime dateTime);
    List<Booking> findAllByBookerAndItemAndStatusEqualsAndEndBefore(User booker, Item item, BookingStatus status, LocalDateTime time);
}
