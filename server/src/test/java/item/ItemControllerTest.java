package item;

import ru.practicum.ShareItServerApplication;
import ru.practicum.item.ItemController;
import ru.practicum.item.comment.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.service.ItemService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = ShareItServerApplication.class)
@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    private final String urlTemplate = "/items";
    private final String headerUserId = "X-Sharer-User-Id";

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private ItemDto makeItemDto(Long id, String name, String description, Boolean available, Long requestId) {
        ItemDto dto = new ItemDto();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        dto.setAvailable(available);
        dto.setRequestId(requestId);
        return dto;
    }

    private CommentDto makeCommentDto(Long id, String text, String authorName, LocalDateTime created) {
        CommentDto dto = new CommentDto();
        dto.setId(id);
        dto.setText(text);
        dto.setAuthorName(authorName);
        dto.setCreated(created);
        return dto;
    }

    @Test
    void createTest() throws Exception {
        ItemDto requestDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L);

        when(itemService.addItem(anyLong(), any(ItemDto.class))).thenReturn(requestDto);

        mvc.perform(post(urlTemplate)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(headerUserId, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestDto)));
    }

    @Test
    void findItemTest() throws Exception {
        CommentDto comment = makeCommentDto(1L, "text", "authorName", LocalDateTime.of(2022, 7, 3, 19, 30, 1));
        ItemDto responseDto = new ItemDto(1L, "name", "description", true, 1L, null, null, List.of(comment));

        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(responseDto);

        mvc.perform(get(urlTemplate + "/1")
                        .header(headerUserId, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseDto)))
                .andExpect(jsonPath("$.comments[0].id").value(is(comment.getId().intValue())));
    }

    @Test
    void findAllTest() throws Exception {
        CommentDto comment = makeCommentDto(1L, "text", "authorName", LocalDateTime.of(2022, 7, 3, 19, 30, 1));
        ItemDto item1 = new ItemDto(1L, "name1", "description1", true, 1L, null, null, List.of(comment));
        ItemDto item2 = new ItemDto(2L, "name2", "description2", true, 1L, null, null, List.of(comment));

        List<ItemDto> newRequests = List.of(item1, item2);

        when(itemService.getUserItems(anyLong())).thenReturn(newRequests);

        mvc.perform(get(urlTemplate)
                        .header(headerUserId, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(content().json(mapper.writeValueAsString(newRequests)));
    }

    @Test
    void findItemsForTenantTest() throws Exception {
        // Предполагается, что метод searchItems(String text) используется контроллером для поиска
        // Если в контроллере другой метод, адаптируйте соответственно.
        ItemDto item1 = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L);
        ItemDto item2 = makeItemDto(2L, "name2", "description2", Boolean.TRUE, 1L);

        List<ItemDto> items = List.of(item1, item2);

        when(itemService.searchItems(any())).thenReturn(items);

        mvc.perform(get(urlTemplate + "/search?text=test")
                        .header(headerUserId, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(content().json(mapper.writeValueAsString(items)));
    }

    @Test
    void updateTest() throws Exception {
        ItemDto requestDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L);

        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(requestDto);

        mvc.perform(patch(urlTemplate + "/1")
                        .content(mapper.writeValueAsString(requestDto))
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestDto)));
    }

    @Test
    void addCommentTest() throws Exception {
        CommentDto comment = makeCommentDto(1L, "text", "authorName", LocalDateTime.of(2022, 7, 3, 19, 30, 1));

        when(itemService.addComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(comment);

        mvc.perform(post(urlTemplate + "/1/comment")
                        .content(mapper.writeValueAsString(comment))
                        .header(headerUserId, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(comment)));
    }
}
