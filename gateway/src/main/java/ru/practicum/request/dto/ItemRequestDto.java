package ru.practicum.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequestDto {
    private Long id;

    @NotBlank(message = "Description must not be blank")
    private String description;

    private LocalDateTime created;
}
