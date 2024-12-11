package ru.practicum.request.service;

import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.item.Item;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.request.ItemRequest;
import ru.practicum.request.ItemRequestRepository;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.NewRequest;
import ru.practicum.request.dto.UpdateRequest;
import ru.practicum.request.mapper.ItemRequestMapper;
import ru.practicum.user.User;
import ru.practicum.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления запросами на предметы.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

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
