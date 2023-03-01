package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.CommentOutDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentOutDto> json;

    @Test
    void testCommentDto() throws Exception {
        var commentDto = new CommentOutDto(1L, "testText", "testAuthorName", LocalDateTime.now());

        JsonContent<CommentOutDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("testText");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("testAuthorName");
        assertThat(result).extractingJsonPathStringValue("$.created").isNotNull();
    }
}
