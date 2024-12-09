package user;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.exception.EmailAlreadyExistsException;
import YandexPracticium.exception.NotFoundException;
import YandexPracticium.exception.ValidationException;
import YandexPracticium.user.dto.UserDto;
import YandexPracticium.user.repository.UserRepository;
import YandexPracticium.user.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;


import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = ShareItServerApplication.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testUserByIdWhenUserIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> userService.getUserEntityById(2L));
        assertTrue(thrown.getMessage().contains("User not found"));
    }

    @Test
    void testCreateUserWhenEmailExist() {
        UserDto newUser = new UserDto(null, "John Doe", "john.doe@mail.com");
        when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(true);

        EmailAlreadyExistsException thrown = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.addUser(newUser);
        });

        assertEquals("Email already exists", thrown.getMessage());
    }

}
