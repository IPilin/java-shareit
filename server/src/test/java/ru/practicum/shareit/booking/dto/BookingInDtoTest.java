package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingInDtoTest {
    @Autowired
    private JacksonTester<BookingInDto> json;

    @Test
    void testBookingShortDto() throws Exception {
        BookingInDto bookingInDto = new BookingInDto(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(1));

        JsonContent<BookingInDto> result = json.write(bookingInDto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isNotNull();
        assertThat(result).extractingJsonPathStringValue("$.end").isNotNull();
    }
}
