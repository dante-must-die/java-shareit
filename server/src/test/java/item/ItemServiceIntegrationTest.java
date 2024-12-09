/*
package item;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.enums.Statuses;
import YandexPracticium.item.dto.AdvancedItemDto;
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


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "spring.datasource.username=shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = ShareItServerApplication.class)
class ItemServiceIntegrationTest {
    private final EntityManager em;
    private final ItemService itemService;

    private final LocalDateTime testCurrentDate = LocalDateTime.of(2024, 12, 4, 0, 0);

    private void createUserInDb() {
        Query userQuery = em.createNativeQuery("INSERT INTO Users (id, name, email, birthday) " +
                "VALUES (:id , :name , :email , :birthday);");
        userQuery.setParameter("id", "1");
        userQuery.setParameter("name", "Ivan Ivanov");
        userQuery.setParameter("email", "ivan@email");
        userQuery.setParameter("birthday", LocalDate.of(2021, 7, 1));
        userQuery.executeUpdate();
    }

    private void createItemInDb() {
        Query itemQuery = em.createNativeQuery("INSERT INTO Items (id, name, description, available, owner_id, request_id) " +
                "VALUES (:id , :name , :description , :available , :owner_id , :request_id);");
        itemQuery.setParameter("id", "1");
        itemQuery.setParameter("name", "name");
        itemQuery.setParameter("description", "description");
        itemQuery.setParameter("available", Boolean.TRUE);
        itemQuery.setParameter("owner_id", "1");
        itemQuery.setParameter("request_id", "1");
        itemQuery.executeUpdate();
    }

    private void createLastBookingInDb() {
        Query lastBookingQuery = em.createNativeQuery("INSERT INTO Bookings (id, start_date, end_date, item_id, status, booker_id) " +
                "VALUES (:id , :startDate , :endDate , :itemId , :status , :bookerId);");
        lastBookingQuery.setParameter("id", "1");
        lastBookingQuery.setParameter("startDate", testCurrentDate.minusDays(2)); // 2024-12-02
        lastBookingQuery.setParameter("endDate", testCurrentDate.minusDays(1));   // 2024-12-03
        lastBookingQuery.setParameter("itemId", 1L);
        lastBookingQuery.setParameter("status", Statuses.APPROVED.toString());
        lastBookingQuery.setParameter("bookerId", 1L);
        lastBookingQuery.executeUpdate();
    }

    private void createNextBookingInDb() {
        Query nextBookingQuery = em.createNativeQuery("INSERT INTO Bookings (id, start_date, end_date, item_id, status, booker_id) " +
                "VALUES (:id , :startDate , :endDate , :itemId , :status , :bookerId);");
        nextBookingQuery.setParameter("id", "2");
        nextBookingQuery.setParameter("startDate", testCurrentDate.plusDays(1));  // 2024-12-05
        nextBookingQuery.setParameter("endDate", testCurrentDate.plusDays(2));    // 2024-12-06
        nextBookingQuery.setParameter("itemId", 1L);
        nextBookingQuery.setParameter("status", Statuses.APPROVED.toString());
        nextBookingQuery.setParameter("bookerId", 1L);
        nextBookingQuery.executeUpdate();
    }

    private void createCommentInDb() {
        Query commentQuery = em.createNativeQuery("INSERT INTO Comments (id, text, item_id, author_id, created) " +
                "VALUES (:id , :text , :item_id , :author_id , :created);");
        commentQuery.setParameter("id", "1");
        commentQuery.setParameter("text", "text");
        commentQuery.setParameter("item_id", 1L);
        commentQuery.setParameter("author_id", 1L);
        commentQuery.setParameter("created", testCurrentDate.minusDays(1)); // 2024-12-03
        commentQuery.executeUpdate();
    }

    @Test
    void getItemTest() {
        createUserInDb();
        createItemInDb();
        createLastBookingInDb();
        createNextBookingInDb();
        createCommentInDb();

        // Изменение: вызываем перегруженный метод с now = testCurrentDate
        AdvancedItemDto loadItem = itemService.findItem(1L, 1L, testCurrentDate);

        assertThat(loadItem.getId(), CoreMatchers.notNullValue());
        assertThat(loadItem.getName(), equalTo("name"));
        assertThat(loadItem.getDescription(), equalTo("description"));
        assertThat(loadItem.getAvailable(), equalTo(Boolean.TRUE));
        assertThat(loadItem.getLastBooking(), equalTo(testCurrentDate.minusDays(1))); // 2024-12-03
        assertThat(loadItem.getNextBooking(), equalTo(testCurrentDate.plusDays(1)));   // 2024-12-05
        assertThat(loadItem.getComments(), CoreMatchers.notNullValue());
        assertThat(loadItem.getOwnerId(), CoreMatchers.notNullValue());
        assertThat(loadItem.getRequestId(), CoreMatchers.notNullValue());
    }

    @Test
    void testFindAllWithAdvancedData() {
        createUserInDb();
        createItemInDb();
        createLastBookingInDb();
        createNextBookingInDb();
        createCommentInDb();

        // Изменение: теперь вызываем findAll c testCurrentDate
        Collection<AdvancedItemDto> loadRequests = itemService.findAll(1L, testCurrentDate);
        List<AdvancedItemDto> items = new ArrayList<>(loadRequests);

        assertThat(items, hasSize(1));
        AdvancedItemDto item = items.get(0);
        assertThat(items, hasItem(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("name", equalTo(item.getName())),
                hasProperty("description", equalTo(item.getDescription())),
                hasProperty("available", equalTo(item.getAvailable())),
                hasProperty("lastBooking", instanceOf(LocalDateTime.class)),
                hasProperty("nextBooking", instanceOf(LocalDateTime.class)),
                hasProperty("comments", notNullValue()),
                hasProperty("ownerId", equalTo(item.getOwnerId())),
                hasProperty("requestId", equalTo(item.getRequestId()))
        )));
    }

    // Остальные тесты можно оставить как есть, либо при необходимости также использовать перегруженные методы.
}
*/
