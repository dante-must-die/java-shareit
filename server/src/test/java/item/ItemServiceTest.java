/*
package item;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.booking.BookingRepository;
import YandexPracticium.exception.NotItemOwnerException;
import YandexPracticium.exception.ValidationException;
import YandexPracticium.item.Item;
import YandexPracticium.item.comment.NewCommentRequest;
import YandexPracticium.item.dto.AdvancedItemDto;
import YandexPracticium.item.dto.ItemDto;
import YandexPracticium.item.dto.NewItemRequest;
import YandexPracticium.item.repository.ItemRepository;
import YandexPracticium.item.service.ItemServiceImpl;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = ShareItServerApplication.class)
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testAddCommentWhenCommentIsEmpty() {
        NewCommentRequest newComment = new NewCommentRequest("comment", 1L, 1L);

        // user
        NewUserRequest newUser = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        UserDto userDto = userService.create(newUser);
        User user = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // item
        NewItemRequest newItem = new NewItemRequest("name", "description", Boolean.TRUE, 1L, 1L);
        Item item = new Item(1L, "name", "description", Boolean.TRUE, user, 1L);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto findItem = itemService.create(1L, newItem);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        //check
        when(bookingRepository.existsByBookerIdAndItemIdAndEndBefore(anyLong(), anyLong(), any())).thenReturn(Boolean.FALSE);
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            itemService.addComment(1L, 1L, newComment);
        });

        assertEquals(String.format("Пользователь %s не может оставить комментарий, " +
                "так как не пользовался вещью %s", user.getName(), item.getName()), thrown.getMessage());
    }

    @Test
    void testDeleteItemWithWrongOwnerId() {
        // user
        NewUserRequest newUser = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        UserDto userDto = userService.create(newUser);
        User user = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // item
        NewItemRequest newItem = new NewItemRequest("name", "description", Boolean.TRUE, 1L, 1L);
        Item item = new Item(1L, "name", "description", Boolean.TRUE, user, 1L);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto findItem = itemService.create(1L, newItem);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        NotItemOwnerException thrown = assertThrows(NotItemOwnerException.class, () -> {
            itemService.delete(2L, 1L);
        });

        assertEquals("Удалить данные вещи может только её владелец", thrown.getMessage());
    }

    @Test
    void testUpdateItemWithWrongOwnerId() {
        // user
        NewUserRequest newUser = new NewUserRequest("john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3)));

        UserDto userDto = userService.create(newUser);
        User user = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // item
        NewItemRequest newItem = new NewItemRequest("name", "description", Boolean.TRUE, 1L, 1L);
        Item item = new Item(1L, "name", "description", Boolean.TRUE, user, 1L);
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto findItem = itemService.create(1L, newItem);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        NotItemOwnerException thrown = assertThrows(NotItemOwnerException.class, () -> {
            itemService.update(1L, null, 2L);
        });

        assertEquals("Редактировать данные вещи может только её владелец", thrown.getMessage());
    }

    @Test
    void testFindItemsForTenantWithBlankText() {
        Collection<ItemDto> items = itemService.findItemsForTenant(1L, null);

        assertEquals(items, new ArrayList<>());
    }

    @Test
    void testFindAllWithWrongOwnerId() {
        when(itemRepository.findAllByUserId(anyLong())).thenReturn(new ArrayList<>());

        Collection<AdvancedItemDto> items = itemService.findAll(999L);

        assertEquals(items, new ArrayList<>());
    }
}
*/
