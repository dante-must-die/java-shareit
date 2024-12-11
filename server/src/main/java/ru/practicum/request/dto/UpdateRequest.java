package ru.practicum.request.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO для обновления существующего запроса на предмет.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class UpdateRequest {
    private Long id;
    private String description;
    private Long requestorId; // userId
    private LocalDateTime created;
}
