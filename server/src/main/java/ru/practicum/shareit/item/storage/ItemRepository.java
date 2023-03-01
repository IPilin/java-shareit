package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerIdOrderById(Long userId, Pageable pageable);

    @Query("select i " +
            "from Item i " +
            "where i.available = true " +
            "and (lower(i.name) like lower(concat('%', ?1, '%')) " +
            "or lower(i.description) like lower(concat('%', ?1, '%')))")
    List<Item> search(String text, Pageable pageable);

    Set<Item> findByRequesterIn(Set<Long> requesters);
}
