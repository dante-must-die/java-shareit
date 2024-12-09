package request;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.item.dto.ItemDto;
import YandexPracticium.request.ItemRequestController;
import YandexPracticium.request.dto.ItemRequestDto;
import YandexPracticium.request.dto.NewRequest;
import YandexPracticium.request.dto.UpdateRequest;
import YandexPracticium.request.service.ItemRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = ShareItServerApplication.class)
@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    private final String urlTemplate = "/requests";
    private final String headerUserId = "X-Sharer-User-Id";

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private ItemRequestDto makeItemRequestDto(Long id, String description, LocalDateTime date, List<ItemDto> items) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(id);
        dto.setDescription(description);
        dto.setCreated(date);
        dto.setItems(items);
        return dto;
    }

    private ItemDto makeItemDto(Long id, String name, String description, Boolean available, Long requestId) {
        ItemDto dto = new ItemDto();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        dto.setAvailable(available);
        dto.setRequestId(requestId);
        return dto;
    }

    @Test
    void createTest() throws Exception {
        ItemDto item = makeItemDto(3L, "itemName", "itemDesc", true, 10L);
        ItemRequestDto requestDto = makeItemRequestDto(1L, "description",
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), List.of(item));

        when(itemRequestService.create(anyLong(), any(NewRequest.class))).thenReturn(requestDto);

        mvc.perform(post(urlTemplate)
                        .content(mapper.writeValueAsString(new NewRequest("description", 2L)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(headerUserId, 1L))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(requestDto)))
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.items[0].id").value(is(item.getId().intValue())))
                .andExpect(jsonPath("$.items[0].name").value(is(item.getName())))
                .andExpect(jsonPath("$.items[0].requestId").value(is(item.getRequestId().intValue())));
    }

    @Test
    void findRequestTest() throws Exception {
        ItemDto item = makeItemDto(3L, "itemName", "itemDesc", true, 10L);
        ItemRequestDto requestDto = makeItemRequestDto(1L, "description",
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), List.of(item));

        when(itemRequestService.findItemRequest(anyLong())).thenReturn(requestDto);

        mvc.perform(get(urlTemplate + "/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestDto)));
    }

    @Test
    void findAllByRequestorIdTest() throws Exception {
        ItemDto item = makeItemDto(3L, "itemName", "itemDesc", true, 10L);
        ItemRequestDto requestDto1 = makeItemRequestDto(1L, "description",
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), List.of(item));
        ItemRequestDto requestDto2 = makeItemRequestDto(2L, "description",
                LocalDateTime.of(2023, 7, 3, 19, 30, 1), List.of(item));

        List<ItemRequestDto> newRequests = List.of(requestDto1, requestDto2);

        when(itemRequestService.findAllByRequestorId(anyLong())).thenReturn(newRequests);

        mvc.perform(get(urlTemplate)
                        .header(headerUserId, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(content().json(mapper.writeValueAsString(newRequests)));
    }

    @Test
    void findAllOfAnotherRequestorsTest() throws Exception {
        ItemDto item = makeItemDto(3L, "itemName", "itemDesc", true, 10L);
        ItemRequestDto requestDto1 = makeItemRequestDto(1L, "description",
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), List.of(item));
        ItemRequestDto requestDto2 = makeItemRequestDto(2L, "description",
                LocalDateTime.of(2023, 7, 3, 19, 30, 1), List.of(item));

        List<ItemRequestDto> newRequests = List.of(requestDto1, requestDto2);

        when(itemRequestService.findAllOfAnotherRequestors(anyLong())).thenReturn(newRequests);

        mvc.perform(get(urlTemplate + "/all")
                        .header(headerUserId, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(content().json(mapper.writeValueAsString(newRequests)));
    }

    @Test
    void updateTest() throws Exception {
        ItemDto item = makeItemDto(3L, "itemName", "itemDesc", true, 10L);
        ItemRequestDto requestDto = makeItemRequestDto(1L, "description",
                LocalDateTime.of(2022, 7, 3, 19, 30, 1), List.of(item));

        when(itemRequestService.update(anyLong(), any(UpdateRequest.class))).thenReturn(requestDto);

        mvc.perform(put(urlTemplate)
                        .content(mapper.writeValueAsString(new UpdateRequest(1L, "description", 2L,
                                LocalDateTime.of(2022, 7, 3, 19, 30, 1))))
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestDto)));
    }

    /*@Test
    void deleteTest() throws Exception {
        mvc.perform(delete(urlTemplate + "/1"))
                .andExpect(status().isOk());

        verify(itemRequestService, times(1)).delete(1L);
    }*/
}
