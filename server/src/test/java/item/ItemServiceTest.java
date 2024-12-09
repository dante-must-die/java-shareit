package item;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.booking.Booking;
import YandexPracticium.booking.BookingRepository;
import YandexPracticium.enums.Statuses;
import YandexPracticium.exception.AccessDeniedException;
import YandexPracticium.exception.NotFoundException;
import YandexPracticium.exception.ValidationException;
import YandexPracticium.item.Item;
import YandexPracticium.item.comment.Comment;
import YandexPracticium.item.comment.CommentDto;
import YandexPracticium.item.comment.CommentRepository;
import YandexPracticium.item.dto.ItemDto;
import YandexPracticium.item.repository.ItemRepository;
import YandexPracticium.item.service.ItemServiceImpl;
import YandexPracticium.request.ItemRequest;
import YandexPracticium.request.ItemRequestRepository;
import YandexPracticium.user.User;
import YandexPracticium.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = ShareItServerApplication.class)
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User user;
    private User otherUser;
    private Item item;
    private ItemDto itemDto;
    private ItemRequest request;
    private Comment comment;
    private CommentDto commentDto;
    private Booking pastBooking;
    private Booking futureBooking;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("UserName");
        user.setEmail("user@mail.com");

        otherUser = new User();
        otherUser.setId(2L);
        otherUser.setName("OtherName");
        otherUser.setEmail("other@mail.com");

        request = new ItemRequest();
        request.setId(10L);

        item = new Item();
        item.setId(100L);
        item.setName("ItemName");
        item.setDescription("ItemDescription");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequest(request);

        itemDto = new ItemDto(100L, "ItemName", "ItemDescription", true, request.getId());

        comment = new Comment();
        comment.setId(1000L);
        comment.setText("CommentText");
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        commentDto = new CommentDto(comment.getId(), comment.getText(), user.getName(), comment.getCreated());

        pastBooking = new Booking();
        pastBooking.setId(200L);
        pastBooking.setItem(item);
        pastBooking.setBooker(user);
        pastBooking.setStatus(Statuses.APPROVED);
        pastBooking.setStart(LocalDateTime.now().minusDays(2));
        pastBooking.setEnd(LocalDateTime.now().minusDays(1));

        futureBooking = new Booking();
        futureBooking.setId(300L);
        futureBooking.setItem(item);
        futureBooking.setBooker(user);
        futureBooking.setStatus(Statuses.APPROVED);
        futureBooking.setStart(LocalDateTime.now().plusDays(1));
        futureBooking.setEnd(LocalDateTime.now().plusDays(2));
    }

    @Test
    void addItem_whenUserNotFound_throwNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            itemService.addItem(1L, itemDto);
        });

        assertEquals("Пользователь с ID 1 не найден", thrown.getMessage());
    }

    @Test
    void addItem_whenRequestNotFound_throwNoSuchElementException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // requestId = 10L, но request не найден
        when(itemRequestRepository.findById(10L)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> {
            itemService.addItem(1L, itemDto);
        });

        assertEquals("Request not found", thrown.getMessage());
    }

    @Test
    void addItem_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(10L)).thenReturn(Optional.of(request));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto created = itemService.addItem(1L, itemDto);

        assertEquals(itemDto.getId(), created.getId());
        assertEquals(itemDto.getName(), created.getName());
        assertEquals(itemDto.getDescription(), created.getDescription());
        assertEquals(itemDto.getAvailable(), created.getAvailable());
        assertEquals(itemDto.getRequestId(), created.getRequestId());
    }

    @Test
    void updateItem_whenItemNotFound_throwNotFoundException() {
        when(itemRepository.findById(100L)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            itemService.updateItem(1L, 100L, itemDto);
        });
        assertTrue(thrown.getMessage().contains("Вещь c ID 100 не найдена"));
    }

    @Test
    void updateItem_whenNotOwner_throwAccessDeniedException() {
        when(itemRepository.findById(100L)).thenReturn(Optional.of(item));
        // owner userId=1, пытаемся обновить от имени userId=2
        AccessDeniedException thrown = assertThrows(AccessDeniedException.class, () -> {
            itemService.updateItem(2L, 100L, itemDto);
        });
        assertEquals("Only owner can update the item", thrown.getMessage());
    }

    @Test
    void updateItem_success() {
        when(itemRepository.findById(100L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto updated = itemService.updateItem(1L, 100L, itemDto);
        assertEquals(itemDto.getName(), updated.getName());
        assertEquals(itemDto.getDescription(), updated.getDescription());
        assertEquals(itemDto.getAvailable(), updated.getAvailable());
    }

    @Test
    void getItemById_whenNotFound_throwNotFoundException() {
        when(itemRepository.findById(100L)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            itemService.getItemById(100L, 1L);
        });
        assertTrue(thrown.getMessage().contains("Вещь c ID 100 не найдена"));
    }

    @Test
    void getItemById_whenUserIsOwner_addBookingsInfoAndComments() {
        when(itemRepository.findById(100L)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(100L)).thenReturn(List.of(comment));
        when(bookingRepository.findLastBookings(eq(100L), eq(Statuses.APPROVED), any(LocalDateTime.class))).thenReturn(List.of(pastBooking));
        when(bookingRepository.findNextBookings(eq(100L), eq(Statuses.APPROVED), any(LocalDateTime.class))).thenReturn(List.of(futureBooking));

        ItemDto found = itemService.getItemById(100L, 1L);
        assertEquals(item.getId(), found.getId());
        assertFalse(found.getComments().isEmpty());
        assertNotNull(found.getLastBooking());
        assertNotNull(found.getNextBooking());
    }

    @Test
    void getItemById_whenUserIsNotOwner_noBookingsInfo() {
        when(itemRepository.findById(100L)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(100L)).thenReturn(List.of(comment));

        ItemDto found = itemService.getItemById(100L, 2L);
        assertEquals(item.getId(), found.getId());
        assertFalse(found.getComments().isEmpty());
        assertNull(found.getLastBooking());
        assertNull(found.getNextBooking());
    }

    @Test
    void getUserItems_success() {
        when(itemRepository.findByOwnerId(1L)).thenReturn(List.of(item));
        when(commentRepository.findByItemId(100L)).thenReturn(List.of(comment));
        when(bookingRepository.findLastBookings(eq(100L), eq(Statuses.APPROVED), any(LocalDateTime.class))).thenReturn(List.of(pastBooking));
        when(bookingRepository.findNextBookings(eq(100L), eq(Statuses.APPROVED), any(LocalDateTime.class))).thenReturn(List.of(futureBooking));

        List<ItemDto> items = itemService.getUserItems(1L);
        assertEquals(1, items.size());
        ItemDto dto = items.get(0);
        assertEquals(item.getId(), dto.getId());
        assertFalse(dto.getComments().isEmpty());
        assertNotNull(dto.getLastBooking());
        assertNotNull(dto.getNextBooking());
    }

    @Test
    void searchItems_whenTextIsBlank_returnEmptyList() {
        List<ItemDto> found = itemService.searchItems("   ");
        assertTrue(found.isEmpty());

        found = itemService.searchItems(null);
        assertTrue(found.isEmpty());
    }

    @Test
    void searchItems_success() {
        when(itemRepository.search("text")).thenReturn(List.of(item));
        List<ItemDto> found = itemService.searchItems("text");
        assertEquals(1, found.size());
        assertEquals(item.getId(), found.get(0).getId());
    }

    @Test
    void addComment_whenNoBooking_throwValidationException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(100L)).thenReturn(Optional.of(item));
        // Нет завершенных бронирований
        when(bookingRepository.existsByBookerIdAndItemIdAndEndBefore(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(false);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            itemService.addComment(1L, 100L, commentDto);
        });

        assertTrue(thrown.getMessage().contains("не может оставить комментарий"));
    }

    @Test
    void addComment_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(100L)).thenReturn(Optional.of(item));
        // Есть хотя бы одно завершенное бронирование
        when(bookingRepository.existsByBookerIdAndItemIdAndEndBefore(eq(1L), eq(100L), any(LocalDateTime.class))).thenReturn(true);

        Comment savedComment = new Comment(comment.getId(), comment.getText(), item, user, comment.getCreated());
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CommentDto created = itemService.addComment(1L, 100L, commentDto);
        assertEquals(commentDto.getId(), created.getId());
        assertEquals(commentDto.getText(), created.getText());
        assertEquals(commentDto.getAuthorName(), created.getAuthorName());
    }
}
