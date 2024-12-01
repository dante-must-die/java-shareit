package YandexPracticium.item.service;


import YandexPracticium.item.comment.CommentDto;
import YandexPracticium.item.comment.NewCommentRequest;
import YandexPracticium.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getUserItems(Long userId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(Long userId, Long itemId, NewCommentRequest commentDto);
}
