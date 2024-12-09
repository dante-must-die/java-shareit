package request;

import ru.practicum.ShareItServerApplication;
import ru.practicum.item.Item;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.request.ItemRequest;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.NewRequest;
import ru.practicum.request.dto.UpdateRequest;
import ru.practicum.request.mapper.ItemRequestMapper;
import ru.practicum.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ContextConfiguration(classes = ShareItServerApplication.class)
public class ItemRequestMapperTest {
    private final LocalDateTime now = LocalDateTime.now();

    private final User user = new User(1L, "user@mail.com", "UserName");
    private final ItemRequest itemRequest = new ItemRequest(1L, "description", user, now);
    private final NewRequest newRequest = new NewRequest("description", 1L);
    private final UpdateRequest updRequest = new UpdateRequest(1L, "description", 1L, now);

    @Test
    public void toItemRequestDtoTest() {
        ItemRequestDto dto = ItemRequestMapper.toItemRequestDto(itemRequest);
        assertThat(dto.getId(), equalTo(1L));
        assertThat(dto.getDescription(), equalTo("description"));
        assertThat(dto.getCreated(), equalTo(now));
        assertThat(dto.getItems(), equalTo(Collections.emptyList()));
    }

    @Test
    public void toItemRequestDtoWithItemsTest() {
        User owner = new User(2L, "owner@mail.com", "OwnerName");
        Item item = new Item(10L, "itemName", "itemDesc", true, owner, itemRequest);
        List<ItemDto> itemDtos = List.of(ItemMapper.toItemDto(item));

        ItemRequestDto dto = ItemRequestMapper.toItemRequestDto(itemRequest, itemDtos);
        assertThat(dto.getId(), equalTo(1L));
        assertThat(dto.getDescription(), equalTo("description"));
        assertThat(dto.getCreated(), equalTo(now));
        assertThat(dto.getItems().size(), equalTo(1));
        assertThat(dto.getItems().get(0).getName(), equalTo("itemName"));
    }

    @Test
    public void toItemRequestTest() {
        ItemRequest ir = ItemRequestMapper.toItemRequest(newRequest, user, now);
        assertThat(ir.getDescription(), equalTo("description"));
        assertThat(ir.getRequester(), equalTo(user));
        assertThat(ir.getCreated(), equalTo(now));
    }

    @Test
    public void updateItemFieldsTest() {
        ItemRequest ir = ItemRequestMapper.updateItemFields(itemRequest, updRequest, user);
        assertThat(ir.getId(), equalTo(itemRequest.getId()));
        assertThat(ir.getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(ir.getRequester(), equalTo(user));
        assertThat(ir.getCreated(), equalTo(now));
    }
}
