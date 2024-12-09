package item;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.item.Item;
import YandexPracticium.item.dto.ItemDto;
import YandexPracticium.item.mapper.ItemMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ContextConfiguration(classes = ShareItServerApplication.class)
public class ItemMapperTest {

    @Test
    void toItemDtoTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("desc");
        item.setAvailable(true);

        ItemDto dto = ItemMapper.toItemDto(item);

        assertThat(dto.getId(), equalTo(1L));
        assertThat(dto.getName(), equalTo("name"));
        assertThat(dto.getDescription(), equalTo("desc"));
        assertThat(dto.getAvailable(), equalTo(true));
        // requestId проверим, если request=null, то requestId=null
        assertThat(dto.getRequestId(), equalTo(null));
    }

    @Test
    void toItemTest() {
        ItemDto dto = new ItemDto(1L, "name", "desc", true, null);
        Item item = ItemMapper.toItem(dto);

        assertThat(item.getId(), equalTo(1L));
        assertThat(item.getName(), equalTo("name"));
        assertThat(item.getDescription(), equalTo("desc"));
        assertThat(item.getAvailable(), equalTo(true));
        // owner и request устанавливются отдельно, проверять тут не нужно
    }
}
