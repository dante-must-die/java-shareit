package user;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.user.User;
import YandexPracticium.user.dto.UserDto;
import YandexPracticium.user.dto.UserDto;
import YandexPracticium.user.service.UserService;
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
class UserServiceIntegrationTest {
    private final EntityManager em;
    private final UserService userService;

    private void createUserInDb() {
        Query userQuery = em.createNativeQuery("INSERT INTO Users (id, name, email) " +
                "VALUES (:id , :name , :email);");
        userQuery.setParameter("id", "1");
        userQuery.setParameter("name", "Ivan Ivanov");
        userQuery.setParameter("email", "ivan@email");
        userQuery.executeUpdate();
    }

    @Test
    void createTest() {
        UserDto newUser = new UserDto(null, "John Doe", "john.doe@mail.com");

        UserDto created = userService.addUser(newUser);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.name = :nameUser", User.class);
        User user = query.setParameter("nameUser", newUser.getName()).getSingleResult();

        MatcherAssert.assertThat(user.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(user.getName(), Matchers.equalTo(newUser.getName()));
        MatcherAssert.assertThat(user.getEmail(), Matchers.equalTo(newUser.getEmail()));
    }

    @Test
    void getUserTest() {
        createUserInDb();

        UserDto loadUsers = userService.getUserById(1L);

        MatcherAssert.assertThat(loadUsers.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(loadUsers.getName(), Matchers.equalTo("Ivan Ivanov"));
        MatcherAssert.assertThat(loadUsers.getEmail(), Matchers.equalTo("ivan@email"));
    }

    @Test
    void testGetAllUsers() {
        List<UserDto> newUsers = List.of(
                new UserDto(null, "Ivan Ivanov", "ivan@email"),
                new UserDto(null, "Petr Petrov", "petr@mail.com"),
                new UserDto(null, "Vasilii Vasiliev", "vasilii@mail.com"));

        for (UserDto u : newUsers) {
            userService.addUser(u);
        }

        Collection<UserDto> loadUsers = userService.getAllUsers();

        MatcherAssert.assertThat(loadUsers, hasSize(newUsers.size()));
        for (UserDto u : newUsers) {
            MatcherAssert.assertThat(loadUsers, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(u.getName())),
                    hasProperty("email", equalTo(u.getEmail()))
            )));
        }
    }

    @Test
    void updateUserTest() {
        createUserInDb();
        UserDto updUser = new UserDto(null, "John Doe", "john.doe1@mail.com");
        UserDto findUser = userService.updateUser(1L, updUser);

        MatcherAssert.assertThat(findUser.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(findUser.getName(), Matchers.equalTo(updUser.getName()));
        MatcherAssert.assertThat(findUser.getEmail(), Matchers.equalTo(updUser.getEmail()));
    }

    @Test
    void deleteUserTest() {
        createUserInDb();

        userService.deleteUser(1L);

        TypedQuery<User> selectQuery = em.createQuery("Select u from User u where u.name like :nameUser", User.class);
        List<User> users = selectQuery.setParameter("nameUser", "Ivan Ivanov").getResultList();

        MatcherAssert.assertThat(users, equalTo(new ArrayList<>()));
    }
}
