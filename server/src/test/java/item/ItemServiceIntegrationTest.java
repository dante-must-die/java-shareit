package item;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.item.dto.ItemDto;
import YandexPracticium.item.service.ItemService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(
        properties = "spring.datasource.username=shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = ShareItServerApplication.class)
class ItemServiceIntegrationTest {
    private final EntityManager em;
    private final ItemService itemService;

    @Test
    void createAndGetItemTest() {
        Query userQuery = em.createNativeQuery("INSERT INTO USERS (id, name, email) VALUES (1, 'Ivan', 'ivan@email')");
        userQuery.executeUpdate();

        ItemDto newItem = new ItemDto(null, "Drill", "Electric drill", true, null);
        ItemDto createdItem = itemService.addItem(1L, newItem);

        assertThat(createdItem.getId(), notNullValue());
        assertThat(createdItem.getName(), equalTo("Drill"));

        // Проверим получение
        ItemDto loadedItem = itemService.getItemById(createdItem.getId(), 1L);
        assertThat(loadedItem.getId(), equalTo(createdItem.getId()));
        assertThat(loadedItem.getDescription(), equalTo("Electric drill"));
        assertThat(loadedItem.getAvailable(), equalTo(true));
    }

    @Test
    void searchItemsTest() {
        // Создадим пользователя
        Query userQuery = em.createNativeQuery(
                "INSERT INTO USERS (id, name, email) VALUES (2, 'Petr', 'petr@mail')"
        );
        userQuery.executeUpdate();

        // Создадим вещь
        Query itemQuery = em.createNativeQuery(
                "INSERT INTO ITEMS (id, name, description, is_available, owner_id) " +
                        "VALUES (2, 'Hammer', 'Steel hammer', true, 2)"
        );
        itemQuery.executeUpdate();

        List<ItemDto> found = itemService.searchItems("hammer");
        assertThat(found, hasSize(1));
        assertThat(found.get(0).getName(), equalTo("Hammer"));
    }
}
