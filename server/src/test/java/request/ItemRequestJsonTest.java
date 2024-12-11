package request;

import ru.practicum.ShareItServerApplication;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.request.dto.ItemRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ShareItServerApplication.class)
@JsonTest
public class ItemRequestJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDto> requestJson;

    @Test
    void testItemRequestDto() throws Exception {
        final LocalDateTime now = LocalDateTime.now();

        ItemDto item = new ItemDto(1L, "name", "desc", true, 10L);
        final List<ItemDto> items = List.of(item);
        final ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "description", now, items);

        var result = requestJson.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathStringValue("$.created").isNotNull();
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("name");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].requestId").isEqualTo(10);
    }
}
