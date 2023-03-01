package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Booking;

import javax.persistence.*;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "items")
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;
    String name;
    String description;
    @Column(name = "is_available")
    Boolean available;
    @Column(name = "owner_id")
    Long ownerId;
    @Column(name = "request_id")
    Long requester;
    @Transient
    Set<Comment> comments;
    @Transient
    Booking lastBooking;
    @Transient
    Booking nextBooking;

    public void update(Item item) {
        Optional.ofNullable(item.getName()).ifPresent((name) -> this.name = name);
        Optional.ofNullable(item.getDescription()).ifPresent((description) -> this.description = description);
        Optional.ofNullable(item.getAvailable()).ifPresent((available) -> this.available = available);
    }
}
