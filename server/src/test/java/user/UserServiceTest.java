/*
package user;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.exception.DuplicatedDataException;
import YandexPracticium.exception.NotFoundException;
import YandexPracticium.exception.ValidationException;
import YandexPracticium.user.User;
import YandexPracticium.user.dto.NewUserRequest;
import YandexPracticium.user.dto.UpdateUserRequest;
import YandexPracticium.user.repository.UserRepository;
import YandexPracticium.user.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;


import java.time.LocalDate;
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
    void testUserByIdWhenUserIdIsNull() {
        when((userRepository).findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findUser(2L));
    }

    @Test
    void testCreateUserWhenEMailExist() {
        NewUserRequest newUser = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(new User(1L, "john.doe@mail.com", "Some User",
                LocalDate.of(2022, 7, 3))));

        DuplicatedDataException thrown = assertThrows(DuplicatedDataException.class, () -> {
            userService.create(newUser);
        });

        assertEquals(String.format("Этот E-mail \"%s\" уже используется", newUser.getEmail()), thrown.getMessage());
    }

    @Test
    void testUpdateUserWhenUserWithSameEmail() {
        UpdateUserRequest newUser = new UpdateUserRequest(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(new User(1L, "john.doe@mail.com", "Some User",
                LocalDate.of(2022, 7, 3))));

        DuplicatedDataException thrown = assertThrows(DuplicatedDataException.class, () -> {
            userService.update(1L, newUser);
        });

        assertEquals(String.format("Этот E-mail \"%s\" уже используется", newUser.getEmail()), thrown.getMessage());
    }

    @Test
    void testUpdateUserWhenUserIdIsNull() {
        UpdateUserRequest newUser = new UpdateUserRequest(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            userService.update(null, newUser);
        });

        assertEquals("ID пользователя должен быть указан", thrown.getMessage());
    }
}
*/
