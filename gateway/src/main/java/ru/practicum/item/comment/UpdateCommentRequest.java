package ru.practicum.item.comment;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Запрос для обновления существующего комментария.
 */
@Data
@EqualsAndHashCode(of = {"id"})
public class UpdateCommentRequest {
    private Long id;
    private String text;
}
