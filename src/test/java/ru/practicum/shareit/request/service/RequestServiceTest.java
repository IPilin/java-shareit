package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class RequestServiceTest {
    @Mock
    ItemRequestRepository repository;
    ItemRequestService service;
    @Mock
    UserServiceImpl userService;
    @Mock
    ItemRepository itemRepository;
    User requestor;
    ItemRequest request;
    ItemRequestInDto requestShortDto;

    @BeforeEach
    public void setup() {
        service = new ItemRequestServiceImpl(repository, userService, itemRepository);
        requestor = new User(1L, "name", "email@email.ru");
        request = new ItemRequest(1L, "description", requestor, LocalDateTime.now(), null);
        requestShortDto = new ItemRequestInDto("description");
    }

    @Test
    void save() {
        when(userService.findById(any()))
                .thenReturn(requestor);
        when(repository.save(any()))
                .thenReturn(request);

        var savedRequestDto = service.create(request.getId(), request);

        assertThat(savedRequestDto.getId()).isEqualTo(1L);
        assertThat(savedRequestDto.getDescription()).isEqualTo("description");
        assertThat(savedRequestDto.getCreated()).isNotNull();
    }

    @Test
    void findAllByRequestor() {
        when(repository.findAllByRequesterIdOrderByCreated(any()))
                .thenReturn(List.of(request));

        var requestDtos = new ArrayList<>(service.findAllByOwner(requestor.getId()));

        assertThat(requestDtos.size()).isEqualTo(1);
        assertThat(requestDtos.get(0).getId()).isEqualTo(1L);
        assertThat(requestDtos.get(0).getDescription()).isEqualTo("description");
        assertThat(requestDtos.get(0).getCreated()).isNotNull();
        assertThat(requestDtos.get(0).getItems()).isEqualTo(List.of());
    }

    @Test
    void findAll() {
        request.setRequester(new User(2L, "name2", "email2@email.ru"));

        var requests = new PageImpl<>(List.of(request));

        when(repository.findAll(PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "created"))))
                .thenReturn(requests);

        var requestDtos = new ArrayList<>(service.findAll(requestor.getId(), 0, 20));

        assertThat(requestDtos.size()).isEqualTo(1);
        assertThat(requestDtos.get(0).getId()).isEqualTo(1L);
        assertThat(requestDtos.get(0).getDescription()).isEqualTo("description");
        assertThat(requestDtos.get(0).getCreated()).isNotNull();
        assertThat(requestDtos.get(0).getItems()).isEqualTo(List.of());
    }

    @Test
    void findById() {
        when(userService.findById(any()))
                .thenReturn(requestor);
        when(repository.findById(any()))
                .thenReturn(Optional.of(request));

        var savedRequestDto = service.findById(requestor.getId(), request.getId());

        assertThat(savedRequestDto.getId()).isEqualTo(1L);
        assertThat(savedRequestDto.getDescription()).isEqualTo("description");
        assertThat(savedRequestDto.getCreated()).isNotNull();
        assertThat(savedRequestDto.getItems()).isEqualTo(List.of());
    }
}