package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestInDto;
import ru.practicum.shareit.request.dto.ItemRequestOutDto;
import ru.practicum.shareit.request.service.impl.ItemRequestServiceImpl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class RequestControllerTest {
    private final ItemRequestOutDto requestDto = new ItemRequestOutDto(
            1L,
            "Хотел бы воспользоваться щёткой для обуви",
            LocalDateTime.now(),
            null);
    private final ItemRequestInDto requestShortDto =
            new ItemRequestInDto("Хотел бы воспользоваться щёткой для обуви");
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemRequestServiceImpl service;
    @Autowired
    MockMvc mvc;

    @Test
    void save() throws Exception {
        when(service.create(anyLong(), any()))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestShortDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.items", is(requestDto.getItems())));
    }

    @Test
    void findAllByRequestor() throws Exception {
        when(service.findAllByOwner(any()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$[0].items", is(requestDto.getItems())));
    }

    @Test
    void findAllByRequestorWithException() throws Exception {
        when(service.findAllByOwner(any()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get("/requests").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll() throws Exception {
        when(service.findAll(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests/all").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$[0].items", is(requestDto.getItems())));
    }

    @Test
    void findById() throws Exception {
        when(service.findById(any(), any()))
                .thenReturn(requestDto);

        mvc.perform(get("/requests/{requestId}", requestDto.getId())
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.items", is(requestDto.getItems())));
    }
}
