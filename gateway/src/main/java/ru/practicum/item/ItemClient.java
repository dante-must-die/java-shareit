package ru.practicum.item;

import ru.practicum.client.BaseClient;
import ru.practicum.item.comment.CommentDto;
import ru.practicum.item.dto.ItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;


import java.util.Map;

@Component
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplate restTemplate) {
        super(serverUrl, restTemplate);
    }

    public ResponseEntity<Object> addItem(Long userId, ItemDto itemDto) {
        return post(API_PREFIX, userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, ItemDto itemDto) {
        return patch(API_PREFIX + "/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getItemById(Long userId, Long itemId) {
        return get(API_PREFIX + "/" + itemId, userId);
    }

    public ResponseEntity<Object> getUserItems(Long userId) {
        return get(API_PREFIX, userId);
    }

    public ResponseEntity<Object> searchItems(String text) {
        Map<String, Object> parameters = Map.of("text", text);
        return get(API_PREFIX + "/search", null, parameters);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentDto commentDto) {
        return post(API_PREFIX + "/" + itemId + "/comment", userId, commentDto);
    }
}
