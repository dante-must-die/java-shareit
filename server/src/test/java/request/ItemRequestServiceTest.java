package request;

import ru.practicum.ShareItServerApplication;
import ru.practicum.exception.ValidationException;
import ru.practicum.request.ItemRequestRepository;
import ru.practicum.request.dto.UpdateRequest;
import ru.practicum.request.service.ItemRequestServiceImpl;
import ru.practicum.user.User;
import ru.practicum.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void testUpdateRequestWhenIdIsNull() {
        UpdateRequest updItemRequest = new UpdateRequest(
                null,              // ID не указан
                "description1",
                1L,
                LocalDateTime.of(2023, 7, 3, 19, 30, 1)
        );

        // Мокаем наличие пользователя, т.к. update требует наличия пользователя
        User user = new User(1L, "john.doe@mail.com", "John Doe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            itemRequestService.update(1L, updItemRequest);
        });

        assertEquals("ID запроса должен быть указан", thrown.getMessage());
    }
}
