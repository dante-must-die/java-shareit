package ru.practicum.booking.dto;

import ru.practicum.booking.Status;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;

import lombok.*;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;

    @NotNull(message = "Start date must not be null")
    @Future(message = "Start date must be in the future")
    private LocalDateTime start;

    @NotNull(message = "End date must not be null")
    @Future(message = "End date must be in the future")
    private LocalDateTime end;

    @NotNull(message = "Item ID must not be null")
    private Long itemId;

    private ItemDto item;

    private UserDto booker;

    private Status status;
}
