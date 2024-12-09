/*
package user;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.user.User;
import YandexPracticium.user.dto.NewUserRequest;
import YandexPracticium.user.dto.UpdateUserRequest;
import YandexPracticium.user.dto.UserDto;
import YandexPracticium.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;


import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ContextConfiguration(classes = ShareItServerApplication.class)
public class UserMapperTest {
    private final NewUserRequest newUser = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));
    private final UpdateUserRequest updUser = new UpdateUserRequest(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));
    private final User user = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));
    private final UserDto dto = new UserDto(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

    private final UpdateUserRequest emptyUpdUser = new UpdateUserRequest(1L, "", "", null);

    @Test
    public void toUserDtoTest() {
        UserDto userDto = UserMapper.mapToUserDto(user);
        assertThat(userDto, equalTo(dto));
    }

    @Test
    public void toUserTest() {
        User us = UserMapper.mapToUser(newUser);
        assertThat(us.getName(), equalTo(user.getName()));
        assertThat(us.getEmail(), equalTo(user.getEmail()));
        assertThat(us.getName(), equalTo(user.getName()));
        assertThat(us.getBirthday(), equalTo(user.getBirthday()));
    }

    @Test
    public void updateUserFieldsTest() {
        User us = UserMapper.updateUserFields(user, updUser);
        assertThat(us.getId(), equalTo(user.getId()));
        assertThat(us.getName(), equalTo(user.getName()));
        assertThat(us.getEmail(), equalTo(user.getEmail()));
        assertThat(us.getBirthday(), equalTo(user.getBirthday()));
    }

    @Test
    public void updateUserEmptyFieldsTest() {
        User us = UserMapper.updateUserFields(user, emptyUpdUser);
        assertThat(us.getId(), equalTo(user.getId()));
        assertThat(us.getName(), equalTo(user.getName()));
        assertThat(us.getEmail(), equalTo(user.getEmail()));
        assertThat(us.getBirthday(), equalTo(user.getBirthday()));
    }
}
*/
