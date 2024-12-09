package ru.practicum.request.service;

import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.NewRequest;
import ru.practicum.request.dto.UpdateRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, NewRequest request);

    ItemRequestDto findItemRequest(Long itemRequestId);

    Collection<ItemRequestDto> findAllByRequestorId(Long requestorId);

    Collection<ItemRequestDto> findAllOfAnotherRequestors(Long requestorId);

    ItemRequestDto update(Long user, UpdateRequest request);

    void delete(Long itemRequestId);
}
