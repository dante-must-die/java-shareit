package booking;

import ru.practicum.ShareItServerApplication;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.NewBookingRequest;
import ru.practicum.booking.dto.UpdateBookingRequest;
import ru.practicum.booking.service.BookingServiceImpl;
import ru.practicum.enums.Statuses;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.exception.ItemNotAvailableException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.item.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;
import ru.practicum.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void testFindBookingWhenUserNotItemOwnerOrBooker() {
        User owner = new User(1L, "John Doe", "john.doe@mail.com");
        User booker = new User(2L, "Petr Petrov", "petr@mail.com");
        User stranger = new User(999L, "Stranger", "stranger@mail.com");
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
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            bookingService.findBooking(1L, 999L);
        });

        assertEquals("Только владелец вещи и создатель брони могут просматривать данные о бронировании", thrown.getMessage());
    }


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

    @Test
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
        UpdateBookingRequest updBooking = new UpdateBookingRequest(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L,
                Statuses.REJECTED,
                1L
        );

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            bookingService.update(3L, updBooking);
        });

        assertEquals("Пользователь создающий бронирование c ID 3 не найден", thrown.getMessage());
    }


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

    @Test
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


        // Изменяем тип ожидаемого исключения на ItemNotAvailableException
        ItemNotAvailableException thrown = assertThrows(ItemNotAvailableException.class, () -> {
            bookingService.approveBooking(1L, 1L, Boolean.FALSE);
        });

        assertEquals("Вещь уже занята!", thrown.getMessage());
    }

    @Test
    void testCreateBookingSuccess() {
        User ownerLocal = new User(1L, "John Doe", "john.doe@mail.com");
        User bookerLocal = new User(2L, "Petr Petrov", "petr@mail.com");
        ItemRequest requestLocal = new ItemRequest(1L, "Need a hammer", ownerLocal, LocalDateTime.now());
        Item itemLocal = new Item(1L, "name", "description", true, ownerLocal, requestLocal);

        when(userRepository.findById(2L)).thenReturn(Optional.of(bookerLocal));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(itemLocal));

        NewBookingRequest newBooking = new NewBookingRequest(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                1L,
                2L
        );

        Booking savedBooking = new Booking(1L, newBooking.getStart(), newBooking.getEnd(), itemLocal, Statuses.WAITING, bookerLocal);
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        BookingDto result = bookingService.create(2L, newBooking);
        assertNotNull(result.getId());
        assertEquals(Statuses.WAITING, result.getStatus());
        assertEquals(itemLocal.getId(), result.getItem().getId());
    }

    @Test
    void testFindBookingOwnerSuccess() {
        User ownerLocal = new User(1L, "John Doe", "john.doe@mail.com");
        User bookerLocal = new User(2L, "Petr Petrov", "petr@mail.com");
        ItemRequest requestLocal = new ItemRequest(1L, "Need a hammer", ownerLocal, LocalDateTime.now());
        Item itemLocal = new Item(1L, "name", "description", true, ownerLocal, requestLocal);
        Booking bookingLocal = new Booking(1L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), itemLocal, Statuses.WAITING, bookerLocal);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingLocal));
        when(userRepository.findById(ownerLocal.getId())).thenReturn(Optional.of(ownerLocal));

        BookingDto result = bookingService.findBooking(1L, 1L);
        assertEquals(bookingLocal.getId(), result.getId());
        assertEquals(Statuses.WAITING, result.getStatus());
    }


    @Test
    void testUpdateBookingByBookerSuccess() {
        User ownerLocal = new User(1L, "John Doe", "john.doe@mail.com");
        User bookerLocal = new User(2L, "Petr Petrov", "petr@mail.com");
        ItemRequest requestLocal = new ItemRequest(1L, "Need a hammer", ownerLocal, LocalDateTime.now());
        Item itemLocal = new Item(1L, "name", "description", true, ownerLocal, requestLocal);
        Booking bookingLocal = new Booking(1L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), itemLocal, Statuses.WAITING, bookerLocal);

        UpdateBookingRequest updBooking = new UpdateBookingRequest(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                1L,
                Statuses.REJECTED,
                2L
        );

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingLocal));
        when(userRepository.findById(2L)).thenReturn(Optional.of(bookerLocal));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDto result = bookingService.update(2L, updBooking);
        assertEquals(Statuses.REJECTED, result.getStatus());
    }


    @Test
    void testDeleteBooking() {
        User ownerLocal = new User(1L, "John Doe", "john.doe@mail.com");
        User bookerLocal = new User(2L, "Petr Petrov", "petr@mail.com");
        ItemRequest requestLocal = new ItemRequest(1L, "Need a hammer", ownerLocal, LocalDateTime.now());
        Item itemLocal = new Item(1L, "name", "description", true, ownerLocal, requestLocal);
        Booking bookingLocal = new Booking(1L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), itemLocal, Statuses.WAITING, bookerLocal);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingLocal));
        bookingService.delete(1L);
        verify(bookingRepository, times(1)).delete(bookingLocal);
    }

    // Покрываем все состояния findAllBookingsByUser
    @Test
    void testFindAllBookingsByUserAll() {
        User bookerLocal = new User(2L, "Petr Petrov", "petr@mail.com");
        User ownerLocal = new User(1L, "John Doe", "john.doe@mail.com");
        ItemRequest req = new ItemRequest(1L, "Need a hammer", ownerLocal, LocalDateTime.now());
        Item itemLocal = new Item(1L, "name", "desc", true, ownerLocal, req);
        Booking bookingLocal = new Booking(1L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), itemLocal, Statuses.WAITING, bookerLocal);

        when(userRepository.findById(2L)).thenReturn(Optional.of(bookerLocal));
        when(bookingRepository.findAllByBookerId(2L)).thenReturn(List.of(bookingLocal));

        Collection<BookingDto> result = bookingService.findAllBookingsByUser(2L, "ALL");
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllBookingsByUserCurrent() {
        User bookerLocal = new User(2L, "Petr Petrov", "petr@mail.com");
        User ownerLocal = new User(1L, "John", "john@mail.com");
        ItemRequest req = new ItemRequest(1L, "Need a hammer", ownerLocal, LocalDateTime.now());
        Item itemLocal = new Item(1L, "n", "d", true, ownerLocal, req);
        // текущий: start < now < end
        Booking bookingLocal = new Booking(1L, LocalDateTime.now().minusMinutes(30),
                LocalDateTime.now().plusMinutes(30), itemLocal, Statuses.WAITING, bookerLocal);

        when(userRepository.findById(2L)).thenReturn(Optional.of(bookerLocal));
        when(bookingRepository.findAllCurrentBookingByBookerId(2L)).thenReturn(List.of(bookingLocal));

        Collection<BookingDto> result = bookingService.findAllBookingsByUser(2L, "CURRENT");
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllBookingsByUserPast() {
        User bookerLocal = new User(2L, "Petr", "p@mail.com");
        User ownerLocal = new User(1L, "John", "j@mail.com");
        ItemRequest req = new ItemRequest(1L, "r", ownerLocal, LocalDateTime.now());
        Item itemLocal = new Item(1L, "n", "d", true, ownerLocal, req);
        // past: now > end
        Booking bookingLocal = new Booking(1L, LocalDateTime.now().minusHours(2),
                LocalDateTime.now().minusHours(1), itemLocal, Statuses.WAITING, bookerLocal);

        when(userRepository.findById(2L)).thenReturn(Optional.of(bookerLocal));
        when(bookingRepository.findAllPastBookingByBookerId(2L)).thenReturn(List.of(bookingLocal));

        Collection<BookingDto> result = bookingService.findAllBookingsByUser(2L, "PAST");
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllBookingsByUserFuture() {
        User bookerLocal = new User(2L, "Petr", "p@mail.com");
        User ownerLocal = new User(1L, "John", "j@mail.com");
        ItemRequest req = new ItemRequest(1L, "r", ownerLocal, LocalDateTime.now());
        Item itemLocal = new Item(1L, "n", "d", true, ownerLocal, req);
        // future: now < start
        Booking bookingLocal = new Booking(1L, LocalDateTime.now().plusMinutes(10),
                LocalDateTime.now().plusMinutes(20), itemLocal, Statuses.WAITING, bookerLocal);

        when(userRepository.findById(2L)).thenReturn(Optional.of(bookerLocal));
        when(bookingRepository.findAllFutureBookingByBookerId(2L)).thenReturn(List.of(bookingLocal));

        Collection<BookingDto> result = bookingService.findAllBookingsByUser(2L, "FUTURE");
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllBookingsByUserWaiting() {
        User bookerLocal = new User(2L, "Petr", "p@mail.com");
        User ownerLocal = new User(1L, "John", "j@mail.com");
        ItemRequest req = new ItemRequest(1L, "r", ownerLocal, LocalDateTime.now());
        Item itemLocal = new Item(1L, "n", "d", true, ownerLocal, req);
        Booking bookingLocal = new Booking(1L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), itemLocal, Statuses.WAITING, bookerLocal);

        when(userRepository.findById(2L)).thenReturn(Optional.of(bookerLocal));
        when(bookingRepository.findAllByBookerIdAndStatus(2L, Statuses.WAITING)).thenReturn(List.of(bookingLocal));

        Collection<BookingDto> result = bookingService.findAllBookingsByUser(2L, "WAITING");
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllBookingsByUserRejected() {
        User bookerLocal = new User(2L, "Petr", "p@mail.com");
        User ownerLocal = new User(1L, "John", "j@mail.com");
        ItemRequest req = new ItemRequest(1L, "r", ownerLocal, LocalDateTime.now());
        Item itemLocal = new Item(1L, "n", "d", true, ownerLocal, req);
        Booking bookingLocal = new Booking(1L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), itemLocal, Statuses.REJECTED, bookerLocal);

        when(userRepository.findById(2L)).thenReturn(Optional.of(bookerLocal));
        when(bookingRepository.findAllByBookerIdAndStatus(2L, Statuses.REJECTED)).thenReturn(List.of(bookingLocal));

        Collection<BookingDto> result = bookingService.findAllBookingsByUser(2L, "REJECTED");
        assertEquals(1, result.size());
    }

    // Покрываем все состояния findAllBookingsByOwnerItems
    @Test
    void testFindAllBookingsByOwnerAll() {
        User ownerLocal = new User(1L, "John", "j@mail.com");
        User bookerLocal = new User(2L, "Petr", "p@mail.com");
        ItemRequest req = new ItemRequest(1L, "r", ownerLocal, LocalDateTime.now());
        Item itemLocal = new Item(1L, "n", "d", true, ownerLocal, req);
        Booking bookingLocal = new Booking(1L, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2), itemLocal, Statuses.WAITING, bookerLocal);

        when(userRepository.findById(1L)).thenReturn(Optional.of(ownerLocal));
        when(bookingRepository.findAllByOwnerId(1L)).thenReturn(List.of(bookingLocal));

        Collection<BookingDto> result = bookingService.findAllBookingsByOwnerItems(1L, "ALL");
        assertEquals(1, result.size());
    }

}





