package ru.practicum.user;

import ru.practicum.client.BaseClient;
import ru.practicum.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplate restTemplate) {
        super(serverUrl, restTemplate);
    }

    public ResponseEntity<Object> addUser(UserDto userDto) {
        return post(API_PREFIX, userDto);
    }

    public ResponseEntity<Object> updateUser(Long userId, UserDto userDto) {
        return patch(API_PREFIX + "/" + userId, userId, userDto);
    }

    public ResponseEntity<Object> getUser(Long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> getUserById(Long userId) {
        return get(API_PREFIX + "/" + userId, userId);
    }

    public ResponseEntity<Object> getAllUsers() {
        return get(API_PREFIX, null);
    }

    public ResponseEntity<Object> deleteUser(Long userId) {
        return delete(API_PREFIX + "/" + userId, userId);
    }
}
