package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.marker.OnCreate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
	@NotNull(groups = OnCreate.class)
	private long itemId;
	@FutureOrPresent(groups = OnCreate.class)
	private LocalDateTime start;
	@Future(groups = OnCreate.class)
	private LocalDateTime end;
}
