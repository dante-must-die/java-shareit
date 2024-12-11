package ru.practicum.item.service;

import ru.practicum.item.comment.CommentDto;
import ru.practicum.item.dto.ItemDto;

import java.util.List;

/**
 * Интерфейс сервиса для управления предметами.
 */
public interface ItemService {
    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getUserItems(Long userId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);
}
