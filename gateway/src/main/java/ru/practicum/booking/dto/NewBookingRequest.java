package ru.practicum.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO для создания нового запроса на бронирование.
 */
@Data
public class NewBookingRequest {

    private LocalDateTime start;

    private LocalDateTime end;

    @NotNull(message = "У искомой вещи должен быть ID")
    @Positive(message = "ID вещи не может быть отрицательным числом")
    private Long itemId;

    @Positive(message = "ID пользователя который хочет забронировать вещь не может быть отрицательным числом")
    private Long bookerId;
}
