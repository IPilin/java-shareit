package ru.practicum.shareit.user.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "users")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    String name;
    @Column(unique = true)
    String email;

    public void update(User user) {
        Optional.ofNullable(user.getName()).ifPresent((name) -> this.name = name);
        Optional.ofNullable(user.getEmail()).ifPresent((email) -> this.email = email);
    }
}
