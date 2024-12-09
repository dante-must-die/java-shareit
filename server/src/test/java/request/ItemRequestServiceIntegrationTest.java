package request;

import ru.practicum.ShareItServerApplication;
import ru.practicum.request.ItemRequest;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.NewRequest;
import ru.practicum.request.dto.UpdateRequest;
import ru.practicum.request.service.ItemRequestService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "spring.datasource.username=shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = ShareItServerApplication.class)
class ItemRequestServiceIntegrationTest {
    private final EntityManager em;
    private final ItemRequestService itemRequestService;

    private void createUserInDb() {
        Query userQuery = em.createNativeQuery("INSERT INTO USERS (id, name, email) " +
                "VALUES (:id , :name , :email);");
        userQuery.setParameter("id", "1");
        userQuery.setParameter("name", "Ivan Ivanov");
        userQuery.setParameter("email", "ivan@email");
        userQuery.executeUpdate();
    }

    private void createRequestInDb() {
        Query requestQuery = em.createNativeQuery("INSERT INTO REQUESTS (id, description, requester_id, created) " +
                "VALUES (:id , :description , :requester_id , :created);");
        requestQuery.setParameter("id", "1");
        requestQuery.setParameter("description", "description");
        requestQuery.setParameter("requester_id", "1");
        requestQuery.setParameter("created", LocalDateTime.of(2022, 7, 3, 19, 30, 1));
        requestQuery.executeUpdate();
    }

    @Test
    void createItemRequestTest() {
        createUserInDb();

        NewRequest newRequest = new NewRequest("description", 1L);
        ItemRequestDto findItemRequest = itemRequestService.create(1L, newRequest);

        MatcherAssert.assertThat(findItemRequest.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(findItemRequest.getDescription(), Matchers.equalTo(newRequest.getDescription()));
        MatcherAssert.assertThat(findItemRequest.getCreated(), notNullValue());
    }

    @Test
    void getItemRequestTest() {
        createUserInDb();
        createRequestInDb();

        ItemRequestDto loadRequest = itemRequestService.findItemRequest(1L);

        MatcherAssert.assertThat(loadRequest.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(loadRequest.getDescription(), Matchers.equalTo("description"));
        MatcherAssert.assertThat(loadRequest.getCreated(), Matchers.equalTo(LocalDateTime.of(2022, 7, 3, 19, 30, 1)));
    }

    @Test
    void testFindAllByRequestorId() {
        createUserInDb();

        List<NewRequest> itemRequests = List.of(
                new NewRequest("description1", 1L),
                new NewRequest("description2", 1L),
                new NewRequest("description3", 1L)
        );

        for (NewRequest itemRequest : itemRequests) {
            itemRequestService.create(1L, itemRequest);
        }

        Collection<ItemRequestDto> loadRequests = itemRequestService.findAllByRequestorId(1L);

        MatcherAssert.assertThat(loadRequests, hasSize(itemRequests.size()));
        for (NewRequest ir : itemRequests) {
            MatcherAssert.assertThat(loadRequests, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(ir.getDescription())),
                    hasProperty("created", notNullValue()),
                    hasProperty("items", equalTo(new ArrayList<>()))
            )));
        }
    }

    @Test
    void testFindAllOfAnotherRequestors() {
        createUserInDb(); // Создаёт пользователя с ID=1

        // Создаём второго пользователя, чтобы findAllOfAnotherRequestors(2L) не выкинул исключение
        Query userQuery2 = em.createNativeQuery("INSERT INTO USERS (id, name, email) VALUES (:id , :name , :email);");
        userQuery2.setParameter("id", "2");
        userQuery2.setParameter("name", "Petr Petrov");
        userQuery2.setParameter("email", "petr@email");
        userQuery2.executeUpdate();

        List<NewRequest> itemRequests = List.of(
                new NewRequest("description1", 1L),
                new NewRequest("description2", 1L),
                new NewRequest("description3", 1L)
        );

        for (NewRequest ir : itemRequests) {
            itemRequestService.create(1L, ir);
        }

        // Теперь пользователь с ID=2 существует, и сервис не выбросит NotFoundException
        Collection<ItemRequestDto> loadRequests = itemRequestService.findAllOfAnotherRequestors(2L);

        MatcherAssert.assertThat(loadRequests, hasSize(itemRequests.size()));
        for (NewRequest ir : itemRequests) {
            MatcherAssert.assertThat(loadRequests, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(ir.getDescription())),
                    hasProperty("created", notNullValue()),
                    hasProperty("items", equalTo(new ArrayList<>()))
            )));
        }
    }


    @Test
    void updateItemRequestTest() {
        createUserInDb();
        createRequestInDb();

        UpdateRequest updItemRequest = new UpdateRequest(1L, "description1", 1L,
                LocalDateTime.of(2023, 7, 3, 19, 30, 1));
        ItemRequestDto findItemRequest = itemRequestService.update(1L, updItemRequest);

        MatcherAssert.assertThat(findItemRequest.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(findItemRequest.getDescription(), Matchers.equalTo(updItemRequest.getDescription()));
        MatcherAssert.assertThat(findItemRequest.getCreated(), Matchers.equalTo(updItemRequest.getCreated()));
    }

    @Test
    void deleteItemRequestTest() {
        createUserInDb();
        createRequestInDb();

        itemRequestService.delete(1L);

        TypedQuery<ItemRequest> selectQuery = em.createQuery("Select r from ItemRequest r where r.description = :description", ItemRequest.class);
        List<ItemRequest> requests = selectQuery.setParameter("description", "description").getResultList();

        MatcherAssert.assertThat(requests, equalTo(new ArrayList<>()));
    }
}
