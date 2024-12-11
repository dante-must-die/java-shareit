package ru.practicum.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Объект передачи данных для запроса на предмет.
 */
@Data
public class ItemRequestDto {
    private Long id;

    @NotBlank(message = "Description must not be blank")
    private String description;

    private LocalDateTime created;
}
