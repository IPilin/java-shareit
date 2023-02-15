package ru.practicum.shareit.exception.model;

import lombok.Data;

@Data
public class ErrorResponse {
    public final String error;
    public final String description;
}
