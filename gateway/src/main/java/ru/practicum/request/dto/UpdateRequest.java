package ru.practicum.request.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Запрос для обновления существующего запроса на предмет.
 */
@Data
@EqualsAndHashCode(of = {"id"})
public class UpdateRequest {
    private Long id;
    private String description;
    private Long requestorId; // userId
    private LocalDateTime created;
}
