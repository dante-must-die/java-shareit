package YandexPracticium.request;

import YandexPracticium.request.dto.ItemRequestDto;
import YandexPracticium.request.dto.NewRequest;
import YandexPracticium.request.dto.UpdateRequest;
import YandexPracticium.request.service.ItemRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final String id = "/{request-id}";
    private final String all = "/all";

    private final String headerUserId = "X-Sharer-User-Id";
    private final String pvRequestId = "request-id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader(headerUserId) Long userId,
                                 @RequestBody NewRequest itemRequest) {
        return itemRequestService.create(userId, itemRequest);
    }

    @GetMapping(id)
    public ItemRequestDto findItemRequest(@PathVariable(pvRequestId) Long requestId) {
        return itemRequestService.findItemRequest(requestId);
    }

    @GetMapping
    public Collection<ItemRequestDto> findAllByRequestorId(@RequestHeader(headerUserId) Long requestorId) {
        return itemRequestService.findAllByRequestorId(requestorId);
    }

    @GetMapping(all)
    public Collection<ItemRequestDto> findAllOfAnotherRequestors(@RequestHeader(headerUserId) Long requestorId) {
        return itemRequestService.findAllOfAnotherRequestors(requestorId);
    }

    @PutMapping
    public ItemRequestDto update(@RequestHeader(headerUserId) Long userId,
                                 @RequestBody UpdateRequest newItemRequest) {
        return itemRequestService.update(userId, newItemRequest);
    }

    @DeleteMapping(id)
    public void delete(@PathVariable(pvRequestId) Long requestId) {
        itemRequestService.delete(requestId);
    }
}