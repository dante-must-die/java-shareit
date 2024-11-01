package ru.practicum.shareit.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@NoArgsConstructor
public class ItemRequest {
    private Long id;                    // Уникальный идентификатор запроса
    private String description;         // Текст запроса, содержащий описание требуемой вещи
    private User requestor;             // Пользователь, создавший запрос
    private LocalDateTime created;
}
