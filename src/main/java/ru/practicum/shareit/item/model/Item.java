package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id;                    // Уникальный идентификатор вещи
    private String name;                // Краткое название
    private String description;         // Развёрнутое описание
    private Boolean available;          // Статус о том, доступна или нет вещь для аренды
    private User owner;                 // Владелец вещи
    private ItemRequest request;
}
