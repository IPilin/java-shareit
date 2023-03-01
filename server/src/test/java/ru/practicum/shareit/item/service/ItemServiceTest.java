package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.exception.model.WrongAccessException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemServiceTest {
    ItemService service;
    @Mock
    ItemRepository repository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    UserServiceImpl userService;
    @Mock
    CommentRepository commentRepository;
    User user;
    Item item;
    ItemDto itemShortDto;
    Comment comment;

    @BeforeEach
    public void setup() {
        service = new ItemServiceImpl(repository, commentRepository, userService, bookingRepository);
        user = new User(1L, "userName", "user@email.ru");
        item = new Item(1L, "itemName", "itemDescription", true, 1L, null, null, null, null);
        itemShortDto = new ItemDto(1L, "itemName", "itemDescription", true, null, null, null, null);
        comment = new Comment(1L, "comment", item, user);
    }

    @Test
    void save() {
        when(userService.findById(anyLong()))
                .thenReturn(user);
        when(repository.save(any()))
                .thenReturn(item);

        ItemDto savedItemShortDto = service.create(user.getId(), itemShortDto);

        assertThat(savedItemShortDto.getId()).isEqualTo(1L);
        assertThat(savedItemShortDto.getName()).isEqualTo("itemName");
        assertThat(savedItemShortDto.getDescription()).isEqualTo("itemDescription");
        assertThat(savedItemShortDto.getAvailable()).isEqualTo(true);
    }

    @Test
    void update() {
        when(userService.findById(anyLong()))
                .thenReturn(user);
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(repository.save(any()))
                .thenReturn(item);

        itemShortDto.setName("nameUpdated");
        ItemDto updatedItem = service.update(1L, 1L, itemShortDto);

        assertThat(updatedItem.getId()).isEqualTo(1L);
        assertThat(updatedItem.getName()).isEqualTo("nameUpdated");
        assertThat(updatedItem.getDescription()).isEqualTo("itemDescription");
        assertThat(updatedItem.getAvailable()).isEqualTo(true);
        assertThrows(WrongAccessException.class, () -> service.update(2L, 1L, itemShortDto));
    }

    @Test
    void findById() {
        when(userService.findById(anyLong()))
                .thenReturn(user);
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        var foundItemDto = service.findById(user.getId(), itemShortDto.getId());

        assertThat(foundItemDto.getId()).isEqualTo(1L);
        assertThat(foundItemDto.getName()).isEqualTo("itemName");
        assertThat(foundItemDto.getDescription()).isEqualTo("itemDescription");
        assertThat(foundItemDto.getAvailable()).isEqualTo(true);
    }

    @Test
    void findAllByOwnerId() {
        when(userService.findById(anyLong()))
                .thenReturn(user);
        when(repository.findAllByOwnerIdOrderById(anyLong(), any()))
                .thenReturn(List.of(item));

        var itemDtoList = new ArrayList<>(service.getItems(1L, 0, 20));

        assertThat(itemDtoList.size()).isEqualTo(1);
        assertThat(itemDtoList.get(0).getId()).isEqualTo(1L);
        assertThat(itemDtoList.get(0).getName()).isEqualTo("itemName");
        assertThat(itemDtoList.get(0).getDescription()).isEqualTo("itemDescription");
        assertThat(itemDtoList.get(0).getAvailable()).isEqualTo(true);
    }

    @Test
    void search() {
        when(repository.search(anyString(), any()))
                .thenReturn(List.of(item));

        var itemShortDtoList = new ArrayList<>(service.search("item", 0, 20));

        assertThat(itemShortDtoList.size()).isEqualTo(1);
        assertThat(itemShortDtoList.get(0).getId()).isEqualTo(1L);
        assertThat(itemShortDtoList.get(0).getName()).isEqualTo("itemName");
        assertThat(itemShortDtoList.get(0).getDescription()).isEqualTo("itemDescription");
        assertThat(itemShortDtoList.get(0).getAvailable()).isEqualTo(true);

        assertThat(service.search("", 0, 20).size()).isEqualTo(0);
    }

    @Test
    void saveComment() {
        when(userService.findById(anyLong()))
                .thenReturn(user);
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findAllByBookerAndItemAndStatusEqualsAndEndBefore(any(), any(), any(), any()))
                .thenReturn(List.of(new Booking()));
        when(commentRepository.save(any()))
                .thenReturn(comment);

        var commentDto = service.comment(comment, item.getId(), user.getId());

        assertThat(commentDto.getId()).isEqualTo(1L);
        assertThat(commentDto.getText()).isEqualTo("comment");
        assertThat(commentDto.getAuthorName()).isEqualTo("userName");
        assertThat(commentDto.getCreated()).isNotNull();

        when(bookingRepository.findAllByBookerAndItemAndStatusEqualsAndEndBefore(any(), any(), any(), any()))
                .thenReturn(List.of());

        assertThrows(ValidationException.class, () -> service.comment(comment, item.getId(), user.getId()));
    }
}
