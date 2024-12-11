package ru.practicum.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Запрос для создания нового запроса на предмет.
 */
@Data
public class NewRequest {
    @NotBlank(message = "Запрос не должен быть пустым")
    private String description;

    @Positive(message = "ID пользователя составившего заявку не может не может быть отрицательным числом")
    private Long requestorId;
}
