package YandexPracticium.request.service;

import YandexPracticium.exception.NotFoundException;
import YandexPracticium.exception.ValidationException;
import YandexPracticium.item.Item;
import YandexPracticium.item.dto.ItemDto;
import YandexPracticium.item.mapper.ItemMapper;
import YandexPracticium.item.repository.ItemRepository;
import YandexPracticium.request.ItemRequest;
import YandexPracticium.request.ItemRequestRepository;
import YandexPracticium.request.dto.ItemRequestDto;
import YandexPracticium.request.dto.NewRequest;
import YandexPracticium.request.dto.UpdateRequest;
import YandexPracticium.request.mapper.ItemRequestMapper;
import YandexPracticium.user.User;
import YandexPracticium.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemRequestServiceImpl implements ItemRequestService {
    ItemRequestRepository repository;
    UserRepository userRepository;
    ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto create(Long userId, NewRequest request) {
        log.debug("Создаем запись о запросе");

        User requester = findUserById(userId);

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(request, requester, LocalDateTime.now());
        itemRequest = repository.save(itemRequest);

        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto findItemRequest(Long requestId) {
        log.debug("Ищем запись о запросе с ID {}", requestId);
        ItemRequest itemRequest = findById(requestId);

        List<Item> items = itemRepository.findByRequestId(requestId);
        List<ItemDto> itemDtos = items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());

        return ItemRequestMapper.toItemRequestDto(itemRequest, itemDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> findAllByRequestorId(Long requestorId) {
        log.debug("Получаем записи о всех запросах пользователя с ID {}", requestorId);

        findUserById(requestorId);

        List<ItemRequest> requests = repository.findByRequesterIdOrderByCreatedDesc(requestorId);

        return fillRequestsData(requests);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> findAllOfAnotherRequestors(Long requestorId) {
        log.debug("Получаем записи о всех запросах других пользователей для пользователя с ID {}", requestorId);

        findUserById(requestorId);

        List<ItemRequest> requests = repository.findByRequesterIdNotOrderByCreatedDesc(requestorId);

        return fillRequestsData(requests);
    }

    @Override
    @Transactional
    public ItemRequestDto update(Long userId, UpdateRequest updateRequest) {
        log.debug("Обновляем данные запроса");

        User requester = findUserById(userId);

        if (updateRequest.getId() == null) {
            throw new ValidationException("ID запроса должен быть указан");
        }

        ItemRequest itemRequest = findById(updateRequest.getId());

        if (!itemRequest.getRequester().getId().equals(userId)) {
            throw new ValidationException("Только автор запроса может его обновлять");
        }

        itemRequest = ItemRequestMapper.updateItemFields(itemRequest, updateRequest, requester);
        itemRequest = repository.save(itemRequest);

        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    @Transactional
    public void delete(Long requestId) {
        ItemRequest itemRequest = findById(requestId);
        log.debug("Удаляем данные запроса с ID {}", itemRequest.getId());
        repository.delete(itemRequest);
    }

    // Вспомогательные методы
    private ItemRequest findById(Long itemRequestId) {
        return repository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос c ID %d не найден", itemRequestId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c ID %d не найден", userId)));
    }

    private Collection<ItemRequestDto> fillRequestsData(List<ItemRequest> requests) {
        List<Long> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        Map<Long, List<Item>> itemsByRequestId = itemRepository.findByRequestIdIn(requestIds)
                .stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));

        return requests.stream()
                .map(request -> {
                    List<Item> items = itemsByRequestId.getOrDefault(request.getId(), Collections.emptyList());
                    List<ItemDto> itemDtos = items.stream()
                            .map(ItemMapper::toItemDto)
                            .collect(Collectors.toList());
                    return ItemRequestMapper.toItemRequestDto(request, itemDtos);
                })
                .collect(Collectors.toList());
    }
}
