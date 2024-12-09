package booking;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.booking.Booking;
import YandexPracticium.booking.BookingRepository;
import YandexPracticium.booking.dto.NewBookingRequest;
import YandexPracticium.booking.dto.UpdateBookingRequest;
import YandexPracticium.booking.service.BookingServiceImpl;
import YandexPracticium.enums.Statuses;
import YandexPracticium.exception.AccessDeniedException;
import YandexPracticium.exception.ValidationException;
import YandexPracticium.item.Item;
import YandexPracticium.item.repository.ItemRepository;
import YandexPracticium.request.ItemRequest;
import YandexPracticium.user.User;
import YandexPracticium.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = ShareItServerApplication.class)
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void testCreateBookingWhenItemNotAvailable() {
        User user = new User(1L, "John Doe", "john.doe@mail.com");
        ItemRequest request = new ItemRequest(1L, "Need a hammer", user, LocalDateTime.now());
        Item item = new Item(1L, "name", "description", false, user, request);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        NewBookingRequest newBooking = new NewBookingRequest(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                1L,
                1L
        );

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            bookingService.create(1L, newBooking);
        });

        assertEquals("Вещь не доступна для бронирования!", thrown.getMessage());
    }

    @Test
    void testCreateBookingWhenItemUserIsBooker() {
        User user = new User(1L, "John Doe", "john.doe@mail.com");
        ItemRequest request = new ItemRequest(1L, "Need a hammer", user, LocalDateTime.now());
        Item item = new Item(1L, "name", "description", true, user, request);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        NewBookingRequest newBooking = new NewBookingRequest(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                1L,
                1L
        );

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            bookingService.create(1L, newBooking);
        });

        assertEquals("Нельзя бронировать собственную вещь", thrown.getMessage());
    }

    /*@Test
    void testFindBookingWhenUserNotItemOwnerOrBooker() {
        User owner = new User(1L, "John Doe", "john.doe@mail.com");
        User booker = new User(2L, "Petr Petrov", "petr@mail.com");
        ItemRequest request = new ItemRequest(1L, "Need a hammer", owner, LocalDateTime.now());
        Item item = new Item(1L, "name", "description", true, owner, request);

        Booking booking = new Booking(1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                item,
                Statuses.WAITING,
                booker
        );

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            bookingService.findBooking(1L, 999L);
        });

        assertEquals("Только владелец вещи и создатель брони могут просматривать данные о бронировании", thrown.getMessage());
    }*/

    @Test
    void testUpdateBookingWithWrongId() {
        // Нет смысла создавать сущности, так как ошибка валидации по отсутствию ID.
        UpdateBookingRequest updBooking = new UpdateBookingRequest(null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L,
                Statuses.REJECTED,
                1L);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            bookingService.update(1L, updBooking);
        });

        assertEquals("ID бронирования должен быть указан", thrown.getMessage());
    }

    /*@Test
    void testUpdateBookingWithWrongUser() {
        User owner = new User(1L, "John Doe", "john.doe@mail.com");
        User booker = new User(2L, "Petr Petrov", "petr@mail.com");
        ItemRequest request = new ItemRequest(1L, "Need a hammer", owner, LocalDateTime.now());
        Item item = new Item(1L, "name", "description", true, owner, request);

        Booking booking = new Booking(1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                item,
                Statuses.WAITING,
                booker);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));

        // Попытка обновить бронирование пользователем с ID=3, не являющимся ни владельцем, ни бронировавшим
        UpdateBookingRequest updBooking = new UpdateBookingRequest(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L,
                Statuses.REJECTED,
                1L
        );

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            bookingService.update(3L, updBooking);
        });

        assertEquals("Только владелец вещи и создатель брони могут редактировать данные о бронировании", thrown.getMessage());
    }*/

    @Test
    void testApproveBookingWithWrongUser() {
        User owner = new User(1L, "John Doe", "john.doe@mail.com");
        User booker = new User(2L, "Petr Petrov", "petr@mail.com");
        ItemRequest request = new ItemRequest(1L, "Need a hammer", owner, LocalDateTime.now());
        Item item = new Item(1L, "name", "description", true, owner, request);

        Booking booking = new Booking(1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                item,
                Statuses.WAITING,
                booker);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // Попытка одобрить бронирование пользователем, который не владелец вещи
        AccessDeniedException thrown = assertThrows(AccessDeniedException.class, () -> {
            bookingService.approveBooking(1L, 2L, Boolean.TRUE);
        });

        assertEquals("Менять статус вещи может только её владелец", thrown.getMessage());
    }

    /*@Test
    void testApproveBookingWhenStatusNotWaiting() {
        User owner = new User(1L, "John Doe", "john.doe@mail.com");
        User booker = new User(2L, "Petr Petrov", "petr@mail.com");
        ItemRequest request = new ItemRequest(1L, "Need a hammer", owner, LocalDateTime.now());
        Item item = new Item(1L, "name", "description", true, owner, request);

        // Бронирование уже со статусом APPROVED
        Booking booking = new Booking(1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                item,
                Statuses.APPROVED,
                booker);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            bookingService.approveBooking(1L, 1L, Boolean.FALSE);
        });

        assertEquals("Вещь уже занята!", thrown.getMessage());
    }*/
}
