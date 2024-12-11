package ru.practicum.item.dto;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Запрос для обновления существующего предмета.
 */
@Data
@EqualsAndHashCode(of = {"id"})
public class UpdateItemRequest {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    @Positive(message = "ID владельца вещи не может быть отрицательным числом")
    private Long ownerId;
    @Positive(message = "ID запроса на создание вещи не может быть отрицательным числом")
    private Long requestId;

    public boolean hasName() {
        return !StringUtils.isBlank(this.name);
    }

    public boolean hasDescription() {
        return !StringUtils.isBlank(this.description);
    }

    public boolean hasAvailable() {
        return this.available != null;
    }
}
