package YandexPracticium.request.service;

import YandexPracticium.request.dto.ItemRequestDto;
import YandexPracticium.request.dto.NewRequest;
import YandexPracticium.request.dto.UpdateRequest;

import java.util.Collection;
import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, NewRequest request);

    ItemRequestDto findItemRequest(Long itemRequestId);

    Collection<ItemRequestDto> findAllByRequestorId(Long requestorId);

    Collection<ItemRequestDto> findAllOfAnotherRequestors(Long requestorId);

    ItemRequestDto update(Long user, UpdateRequest request);

    void delete(Long itemRequestId);
}
