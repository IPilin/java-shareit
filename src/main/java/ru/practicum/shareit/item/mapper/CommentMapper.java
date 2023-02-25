package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentInDto;
import ru.practicum.shareit.item.model.CommentOutDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentOutDto toDto(Comment comment) {
        return CommentOutDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(LocalDateTime.now())
                .build();
    }

    public static Set<CommentOutDto> toDto(Set<Comment> comments) {
        return comments.stream().map(CommentMapper::toDto).collect(Collectors.toSet());
    }

    public static Comment fromDto(CommentInDto commentInDto) {
        return Comment.builder()
                .text(commentInDto.getText())
                .build();
    }
}
