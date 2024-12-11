package ru.practicum.request;

import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.NewRequest;
import ru.practicum.request.dto.UpdateRequest;
import ru.practicum.request.service.ItemRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Контроллер для управления запросами на предметы.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String ID_PATH = "/{request-id}";
    private static final String ALL_PATH = "/all";
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private static final String PV_REQUEST_ID = "request-id";
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader(HEADER_USER_ID) Long userId,
                                 @RequestBody NewRequest itemRequest) {
        return itemRequestService.create(userId, itemRequest);
    }

    @GetMapping(ID_PATH)
    public ItemRequestDto findItemRequest(@PathVariable(PV_REQUEST_ID) Long requestId) {
        return itemRequestService.findItemRequest(requestId);
    }

    @GetMapping
    public Collection<ItemRequestDto> findAllByRequestorId(@RequestHeader(HEADER_USER_ID) Long requestorId) {
        return itemRequestService.findAllByRequestorId(requestorId);
    }

    @GetMapping(ALL_PATH)
    public Collection<ItemRequestDto> findAllOfAnotherRequestors(@RequestHeader(HEADER_USER_ID) Long requestorId) {
        return itemRequestService.findAllOfAnotherRequestors(requestorId);
    }

    @PutMapping
    public ItemRequestDto update(@RequestHeader(HEADER_USER_ID) Long userId,
                                 @RequestBody UpdateRequest newItemRequest) {
        return itemRequestService.update(userId, newItemRequest);
    }

    @DeleteMapping(ID_PATH)
    public void delete(@PathVariable(PV_REQUEST_ID) Long requestId) {
        itemRequestService.delete(requestId);
    }
}