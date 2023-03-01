package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserServiceImpl service;
    @Mock
    private UserRepository repository;

    @Test
    void save_thenSavedUser() {
        User user = new User();
        UserDto expectedUserDto = new UserDto();
        when(repository.save(any())).thenReturn(user);

        UserDto actualUserDto = service.create(expectedUserDto);

        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void update_Test() {
        var user = new User(1L, "name", "email");
        var userDto = UserMapper.toDto(user);
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(repository.save(any()))
                .thenReturn(user);

        assertEquals(userDto, service.update(1L, userDto));
    }

    @Test
    void findById_whenUserFound_thenReturnedUser() {
        long userId = 0L;
        User user = new User();
        UserDto expectedUserDto = new UserDto();
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        var actualUserDto = UserMapper.toDto(service.findById(userId));

        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void findById_whenUserFound_thenNotFoundExceptionThrown() {
        long userId = 0L;
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(userId));
    }

    @Test
    void findAll() {
        User expectedUser = new User();
        UserDto expectedUserDto = new UserDto();
        when(repository.findAll()).thenReturn(List.of(expectedUser));

        List<UserDto> actualUserDtoList = new ArrayList<>(service.getAll());

        assertEquals(1, actualUserDtoList.size());
        assertEquals(expectedUserDto, actualUserDtoList.get(0));
    }

    @Test
    void deleteById() {
        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}