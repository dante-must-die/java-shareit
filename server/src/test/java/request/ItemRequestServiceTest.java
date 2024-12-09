/*
package request;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.exception.ValidationException;
import YandexPracticium.request.ItemRequestRepository;
import YandexPracticium.request.dto.UpdateRequest;
import YandexPracticium.request.service.ItemRequestServiceImpl;
import YandexPracticium.user.User;
import YandexPracticium.user.dto.NewUserRequest;
import YandexPracticium.user.dto.UserDto;
import YandexPracticium.user.repository.UserRepository;
import YandexPracticium.user.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = ShareItServerApplication.class)
@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testUpdateUserWhenUserWithSameEmail() {
        UpdateRequest updItemRequest = new UpdateRequest(1L, "description1", 1L,
                LocalDateTime.of(2023, 7, 3, 19, 30, 1));

        NewUserRequest newUser = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        UserDto userDto = userService.create(newUser);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3))));

        updItemRequest.setId(null);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            itemRequestService.update(1L, updItemRequest);
        });

        assertEquals("ID запроса должен быть указан", thrown.getMessage());
    }
}
*/
