package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import jakarta.validation.Valid;

import java.util.List;

/**
 * Контроллер для работы с вещами.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(USER_HEADER) Long userId,
                           @Valid @RequestBody ItemDto itemDto) {
        return itemService.addItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USER_HEADER) Long userId,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_HEADER) Long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(value = USER_HEADER, required = false) Long userId,
                               @PathVariable Long itemId) {
        return itemService.getItemById(itemId, userId);
    }


    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(USER_HEADER) Long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }
}
