package ru.practicum.shareit.exception.model;

public class WrongAccessException extends RuntimeException {
    public WrongAccessException(String message) {
        super(message);
    }
}
