package ru.practicum.item.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Запрос для создания нового комментария.
 */
@Data
public class NewCommentRequest {
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;

    @Positive(message = "ID предмета на который пишут комментарий не может быть отрицательным числом")
    private Long itemId;

    @Positive(message = "ID пользователя который который пишет комментарий не может быть отрицательным числом")
    private Long authorId;
}
