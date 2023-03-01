package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RequestRepositoryTest {
    @Autowired
    ItemRequestRepository repository;

    @Autowired
    UserRepository userRepository;

    @Test
    void findAllByRequestorOrderByCreatedDesc() {
        User requester = new User(1L, "name", "email@email.ru");
        userRepository.save(requester);

        var requestShortDto = new ItemRequestInDto("test");
        ItemRequest request = ItemRequestMapper.fromDto(requestShortDto);
        request.setRequester(requester);
        repository.save(request);

        List<ItemRequest> requests = repository.findAllByRequesterIdOrderByCreated(requester.getId());

        assertThat(requests.size()).isEqualTo(1);
        assertThat(requests.get(0)).isEqualTo(request);
    }
}
