package user;

import ru.practicum.ShareItServerApplication;
import ru.practicum.user.User;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ContextConfiguration(classes = ShareItServerApplication.class)
public class UserMapperTest {

    @Test
    public void toUserDtoTest() {
        User user = new User(1L, "John Doe", "john.doe@mail.com");
        UserDto dto = UserMapper.toUserDto(user);
        assertThat(dto.getId(), equalTo(user.getId()));
        assertThat(dto.getName(), equalTo(user.getName()));
        assertThat(dto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    public void toUserTest() {
        UserDto dto = new UserDto(1L, "John Doe", "john.doe@mail.com");
        User user = UserMapper.toUser(dto);
        assertThat(user.getId(), equalTo(dto.getId()));
        assertThat(user.getName(), equalTo(dto.getName()));
        assertThat(user.getEmail(), equalTo(dto.getEmail()));
    }
}
