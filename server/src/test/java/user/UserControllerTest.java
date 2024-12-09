package user;

import ru.practicum.ShareItServerApplication;
import ru.practicum.user.UserController;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = ShareItServerApplication.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    private final String urlTemplate = "/users";
    @Autowired
    ObjectMapper mapper;
    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mvc;

    private UserDto makeUserDto(Long id, String email, String name) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setEmail(email);
        dto.setName(name);
        return dto;
    }

    @Test
    void createUserTest() throws Exception {
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe");

        when(userService.addUser(any())).thenReturn(userDto);

        mvc.perform(post(urlTemplate)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(userDto.getId().intValue())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.name", is(userDto.getName())));
    }

    @Test
    void findUserTest() throws Exception {
        UserDto findUser = makeUserDto(1L, "ivan@email", "Ivan");

        when(userService.getUserById(anyLong())).thenReturn(findUser);

        mvc.perform(get(urlTemplate + "/" + findUser.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(findUser.getId().intValue())))
                .andExpect(jsonPath("$.email", is(findUser.getEmail())))
                .andExpect(jsonPath("$.name", is(findUser.getName())));
    }

    @Test
    void getUsersTest() throws Exception {
        List<UserDto> newUsers = List.of(
                makeUserDto(1L, "ivan@email", "Ivan"),
                makeUserDto(2L, "petr@email", "Petr"));

        when(userService.getAllUsers()).thenReturn(newUsers);

        mvc.perform(get(urlTemplate)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(is(newUsers.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].email").value(is(newUsers.get(0).getEmail())))
                .andExpect(jsonPath("$[0].name").value(is(newUsers.get(0).getName())))
                .andExpect(jsonPath("$[1].id").value(is(newUsers.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].email").value(is(newUsers.get(1).getEmail())))
                .andExpect(jsonPath("$[1].name").value(is(newUsers.get(1).getName())));
    }

    @Test
    void updateTest() throws Exception {
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe");

        when(userService.updateUser(anyLong(), any())).thenReturn(userDto);

        mvc.perform(patch(urlTemplate + "/" + userDto.getId())
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(userDto.getId().intValue())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.name", is(userDto.getName())));
    }

    @Test
    void deleteTest() throws Exception {
        mvc.perform(delete(urlTemplate + "/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(1L);
    }
}
