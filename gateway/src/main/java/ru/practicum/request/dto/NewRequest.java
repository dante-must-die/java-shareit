package ru.practicum.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewRequest {
    @NotBlank(message = "Запрос не должен быть пустым")
    String description;

    @Positive(message = "ID пользователя составившего заявку не может не может быть отрицательным числом")
    Long requestorId;
}
