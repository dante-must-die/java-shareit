package item;

import ru.practicum.ShareItServerApplication;
import ru.practicum.item.comment.CommentDto;
import ru.practicum.item.dto.ItemDto;
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
public class ItemJsonTest {

    @Autowired
    private JacksonTester<ItemDto> itemJson;

    @Test
    void testItemDto() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "name", "description", Boolean.TRUE, 2L);
        var result = itemJson.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
    }

    @Test
    void testItemDtoWithComments() throws Exception {
        CommentDto comment = new CommentDto(1L, "text", "John Doe", LocalDateTime.now());
        ItemDto itemDto = new ItemDto(1L, "name", "description", Boolean.TRUE, 2L,
                null, null, List.of(comment));

        var result = itemJson.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo("John Doe");
    }
}
