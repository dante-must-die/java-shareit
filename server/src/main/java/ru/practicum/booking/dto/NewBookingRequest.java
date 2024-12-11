package ru.practicum.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Запрос для создания нового бронирования.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewBookingRequest {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
}