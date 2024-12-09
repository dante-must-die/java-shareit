package ru.practicum.request.mapper;

import ru.practicum.item.dto.ItemDto;
import ru.practicum.request.ItemRequest;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.NewRequest;
import ru.practicum.request.dto.UpdateRequest;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDto> items) {
        if (itemRequest == null) return null;
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                items
        );
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return toItemRequestDto(itemRequest, List.of());
    }

    public static ItemRequest toItemRequest(NewRequest newRequest, User requester, LocalDateTime created) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(newRequest.getDescription());
        itemRequest.setRequester(requester);
        itemRequest.setCreated(created);
        return itemRequest;
    }

    public static ItemRequest updateItemFields(ItemRequest itemRequest, UpdateRequest updateRequest, User requester) {
        if (updateRequest.getDescription() != null) {
            itemRequest.setDescription(updateRequest.getDescription());
        }
        itemRequest.setRequester(requester);
        if (updateRequest.getCreated() != null) {
            itemRequest.setCreated(updateRequest.getCreated());
        }
        return itemRequest;
    }
}
