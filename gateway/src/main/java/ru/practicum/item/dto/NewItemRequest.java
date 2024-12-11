package ru.practicum.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Запрос для создания нового предмета.
 */
@Data
public class NewItemRequest {
    @NotBlank(message = "Название вещи не должно быть пустым")
    private String name;

    @NotBlank(message = "Описание вещи не должно быть пустым")
    private String description;

    @NotNull(message = "Статус вещи не может быть пустым. Укажите занята вещь или свободна")
    private Boolean available;

    @Positive(message = "ID владельца вещи не может быть отрицательным числом")
    private Long ownerId;

    @Positive(message = "ID запроса на создание вещи не может быть отрицательным числом")
    private Long requestId;

    public boolean hasRequestId() {
        return requestId != null;
    }
}
