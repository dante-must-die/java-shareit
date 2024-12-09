/*
package booking;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.booking.Booking;
import YandexPracticium.booking.BookingRepository;
import YandexPracticium.booking.dto.BookingDto;
import YandexPracticium.booking.dto.NewBookingRequest;
import YandexPracticium.booking.dto.UpdateBookingRequest;
import YandexPracticium.booking.service.BookingService;
import YandexPracticium.enums.States;
import YandexPracticium.enums.Statuses;
import YandexPracticium.item.Item;
import YandexPracticium.item.dto.ItemDto;
import YandexPracticium.item.repository.ItemRepository;
import YandexPracticium.user.User;
import YandexPracticium.user.dto.UserDto;
import YandexPracticium.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(
        properties = "spring.datasource.username=shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = ShareItServerApplication.class)
class BookingServiceIntegrationTest {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User user1;
    private User user2;
    private Item item1;

    @BeforeEach
    void setUp() {
        // Создание пользователя user1
        user1 = new User();
        user1.setName("Ivan Ivanov");
        user1.setEmail("ivan@email");
        user1.setBirthday(LocalDate.of(2021, 7, 1));
        userRepository.save(user1);

        // Создание пользователя user2
        user2 = new User();
        user2.setName("Petr Petrov");
        user2.setEmail("petr@email");
        user2.setBirthday(LocalDate.of(2021, 7, 1));
        userRepository.save(user2);

        // Создание предмета item1
        item1 = new Item();
        item1.setName("name");
        item1.setDescription("description");
        item1.setAvailable(true);
        item1.setUser(user1);
        item1.setRequestId(1L); // Предполагается, что requestId=1 существует
        itemRepository.save(item1);
    }

    */
/**
     * Вспомогательный метод для создания и сохранения бронирования.
     *
     * @param start   Дата и время начала бронирования.
     * @param end     Дата и время окончания бронирования.
     * @param status  Статус бронирования.
     * @param booker  Пользователь, бронирующий предмет.
     * @param item    Предмет бронирования.
     * @return Сохраненное бронирование.
     *//*

    private Booking createBooking(LocalDateTime start, LocalDateTime end, Statuses status, User booker, Item item) {
        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(status);
        booking.setBooker(booker);
        booking.setItem(item);
        return bookingRepository.save(booking);
    }

    @Test
    void createBookingTest() {
        // Подготовка данных
        NewBookingRequest newRequest = new NewBookingRequest(
                LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 10, 19, 30, 15),
                item1.getId(),
                user2.getId()
        );

        // Действие
        BookingDto booking = bookingService.create(user2.getId(), newRequest);

        // Проверка
        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getStart(), equalTo(newRequest.getStart()));
        assertThat(booking.getEnd(), equalTo(newRequest.getEnd()));
        assertThat(booking.getItem(), notNullValue());
        assertThat(booking.getItem().getClass(), equalTo(ItemDto.class));
        assertThat(booking.getStatus(), equalTo(Statuses.WAITING));
        assertThat(booking.getBooker(), notNullValue());
        assertThat(booking.getBooker().getClass(), equalTo(UserDto.class));
    }

    @Test
    void getBookingTest() {
        // Подготовка данных
        Booking bookingEntity = createBooking(
                LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 10, 19, 30, 15),
                Statuses.APPROVED,
                user1,
                item1
        );

        // Действие
        BookingDto loadedBooking = bookingService.findBooking(bookingEntity.getId(), user1.getId());

        // Проверка
        assertThat(loadedBooking.getId(), notNullValue());
        assertThat(loadedBooking.getStart(), equalTo(LocalDateTime.of(2024, 7, 1, 19, 30, 15)));
        assertThat(loadedBooking.getEnd(), equalTo(LocalDateTime.of(2024, 7, 10, 19, 30, 15)));
        assertThat(loadedBooking.getItem(), notNullValue());
        assertThat(loadedBooking.getItem().getClass(), equalTo(ItemDto.class));
        assertThat(loadedBooking.getStatus(), equalTo(Statuses.APPROVED));
        assertThat(loadedBooking.getBooker(), notNullValue());
        assertThat(loadedBooking.getBooker().getClass(), equalTo(UserDto.class));
    }

    @Test
    void findAllBookingsByUserAndStateAllTest() {
        // Подготовка данных
        createBooking(
                LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 10, 19, 30, 15),
                Statuses.APPROVED,
                user1,
                item1
        );

        // Действие
        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByUser(user1.getId(), String.valueOf(States.ALL));

        // Проверка
        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByUserAndStateCurrentTest() {
        // Подготовка данных
        // Текущее бронирование: начало в прошлом, окончание в будущем
        createBooking(
                LocalDateTime.of(2023, 7, 1, 19, 30, 15), // начало в прошлом
                LocalDateTime.now().plusDays(1), // окончание в будущем
                Statuses.APPROVED,
                user1,
                item1
        );

        // Действие
        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByUser(user1.getId(), String.valueOf(States.CURRENT));

        // Проверка
        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByUserAndStatePastTest() {
        // Подготовка данных
        // Прошлое бронирование: начало и окончание в прошлом
        createBooking(
                LocalDateTime.of(2023, 7, 1, 19, 30, 15),
                LocalDateTime.of(2023, 7, 10, 19, 30, 15),
                Statuses.APPROVED,
                user1,
                item1
        );

        // Действие
        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByUser(user1.getId(), String.valueOf(States.PAST));

        // Проверка
        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByUserAndStateFutureTest() {
        // Подготовка данных
        // Будущее бронирование: начало и окончание в будущем
        createBooking(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                Statuses.APPROVED,
                user1,
                item1
        );

        // Действие
        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByUser(user1.getId(), String.valueOf(States.FUTURE));

        // Проверка
        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByUserAndStateWaitingTest() {
        // Подготовка данных
        // Создание бронирования со статусом WAITING
        NewBookingRequest newRequest = new NewBookingRequest(
                LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15),
                item1.getId(),
                user2.getId()
        );
        BookingDto newBooking = bookingService.create(user2.getId(), newRequest);

        // Действие
        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByUser(user2.getId(), String.valueOf(States.WAITING));

        // Проверка
        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.WAITING)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByUserAndStateRejectedTest() {
        // Подготовка данных
        // Создание бронирования и отклонение его
        NewBookingRequest newRequest = new NewBookingRequest(
                LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15),
                item1.getId(),
                user2.getId()
        );
        BookingDto newBooking = bookingService.create(user2.getId(), newRequest);
        bookingService.approveBooking(newBooking.getId(), user1.getId(), Boolean.FALSE);

        // Действие
        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByUser(user2.getId(), String.valueOf(States.REJECTED));

        // Проверка
        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.REJECTED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByItemOwnerIdAndStateAllTest() {
        // Подготовка данных
        createBooking(
                LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 10, 19, 30, 15),
                Statuses.APPROVED,
                user1,
                item1
        );

        // Действие
        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByOwnerItems(user1.getId(), String.valueOf(States.ALL));

        // Проверка
        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByItemOwnerIdAndStateCurrentTest() {
        // Подготовка данных
        // Текущее бронирование: начало в прошлом, окончание в будущем
        createBooking(
                LocalDateTime.of(2023, 7, 1, 19, 30, 15), // начало в прошлом
                LocalDateTime.now().plusDays(1), // окончание в будущем
                Statuses.APPROVED,
                user1,
                item1
        );

        // Действие
        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByOwnerItems(user1.getId(), String.valueOf(States.CURRENT));

        // Проверка
        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByItemOwnerIdAndStatePastTest() {
        // Подготовка данных
        // Прошлое бронирование: начало и окончание в прошлом
        createBooking(
                LocalDateTime.of(2023, 7, 1, 19, 30, 15),
                LocalDateTime.of(2023, 7, 10, 19, 30, 15),
                Statuses.APPROVED,
                user1,
                item1
        );

        // Действие
        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByOwnerItems(user1.getId(), String.valueOf(States.PAST));

        // Проверка
        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByItemOwnerIdAndStateFutureTest() {
        // Подготовка данных
        // Будущее бронирование: начало и окончание в будущем
        createBooking(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                Statuses.APPROVED,
                user1,
                item1
        );

        // Действие
        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByOwnerItems(user1.getId(), String.valueOf(States.FUTURE));

        // Проверка
        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.APPROVED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByItemOwnerIdAndStateWaitingTest() {
        // Подготовка данных
        // Создание бронирования со статусом WAITING
        NewBookingRequest newRequest = new NewBookingRequest(
                LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15),
                item1.getId(),
                user2.getId()
        );
        BookingDto newBooking = bookingService.create(user2.getId(), newRequest);

        // Действие
        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByOwnerItems(user1.getId(), String.valueOf(States.WAITING));

        // Проверка
        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.WAITING)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void findAllBookingsByItemOwnerIdAndStateRejectedTest() {
        // Подготовка данных
        // Создание бронирования и отклонение его
        NewBookingRequest newRequest = new NewBookingRequest(
                LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15),
                item1.getId(),
                user2.getId()
        );
        BookingDto newBooking = bookingService.create(user2.getId(), newRequest);
        bookingService.approveBooking(newBooking.getId(), user1.getId(), Boolean.FALSE);

        // Действие
        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByOwnerItems(user1.getId(), String.valueOf(States.REJECTED));

        // Проверка
        assertThat(loadBookings, hasSize(1));
        for (BookingDto booking : loadBookings) {
            assertThat(loadBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(booking.getStart())),
                    hasProperty("end", equalTo(booking.getEnd())),
                    hasProperty("item", notNullValue()),
                    hasProperty("status", equalTo(Statuses.REJECTED)),
                    hasProperty("booker", notNullValue())
            )));
        }
    }

    @Test
    void updateBookingTest() {
        // Подготовка данных
        Booking bookingEntity = createBooking(
                LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 10, 19, 30, 15),
                Statuses.APPROVED,
                user1,
                item1
        );

        UpdateBookingRequest updateRequest = new UpdateBookingRequest(
                bookingEntity.getId(),
                LocalDateTime.of(2026, 7, 1, 19, 30, 15),
                LocalDateTime.of(2026, 7, 2, 19, 30, 15),
                item1.getId(),
                Statuses.REJECTED,
                user1.getId()
        );

        // Действие
        BookingDto updatedBooking = bookingService.update(user1.getId(), updateRequest);

        // Проверка
        MatcherAssert.assertThat(updateRequest.getId(), equalTo(updatedBooking.getId()));
        MatcherAssert.assertThat(updateRequest.getStart(), equalTo(updatedBooking.getStart()));
        MatcherAssert.assertThat(updateRequest.getEnd(), equalTo(updatedBooking.getEnd()));
        MatcherAssert.assertThat(updateRequest.getItemId(), equalTo(updatedBooking.getItem().getId()));
        MatcherAssert.assertThat(updateRequest.getStatus(), equalTo(updatedBooking.getStatus()));
        MatcherAssert.assertThat(updateRequest.getBookerId(), equalTo(updatedBooking.getBooker().getId()));
    }

    @Test
    void deleteItemTest() {
        // Подготовка данных
        Booking bookingEntity = createBooking(
                LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 10, 19, 30, 15),
                Statuses.APPROVED,
                user1,
                item1
        );

        // Действие
        bookingService.delete(bookingEntity.getId());

        // Проверка
        List<Booking> bookings = bookingRepository.findById(bookingEntity.getId()).stream().toList();
        assertThat(bookings, empty());
    }

    @Test
    void approveBookingTest() {
        // Подготовка данных
        NewBookingRequest newRequest = new NewBookingRequest(
                LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15),
                item1.getId(),
                user2.getId()
        );
        BookingDto newBooking = bookingService.create(user2.getId(), newRequest);

        // Действие
        BookingDto approvedBooking = bookingService.approveBooking(newBooking.getId(), user1.getId(), Boolean.TRUE);

        // Проверка
        assertThat(approvedBooking.getId(), equalTo(newBooking.getId()));
        assertThat(approvedBooking.getStart(), equalTo(newBooking.getStart()));
        assertThat(approvedBooking.getEnd(), equalTo(newBooking.getEnd()));
        assertThat(approvedBooking.getItem(), notNullValue());
        assertThat(approvedBooking.getItem().getClass(), equalTo(ItemDto.class));
        assertThat(approvedBooking.getStatus(), equalTo(Statuses.APPROVED));
        assertThat(approvedBooking.getBooker(), notNullValue());
        assertThat(approvedBooking.getBooker().getClass(), equalTo(UserDto.class));
    }

    @Test
    void notApproveBookingTest() {
        // Подготовка данных
        NewBookingRequest newRequest = new NewBookingRequest(
                LocalDateTime.of(2024, 7, 1, 19, 30, 15),
                LocalDateTime.of(2024, 7, 2, 19, 30, 15),
                item1.getId(),
                user2.getId()
        );
        BookingDto newBooking = bookingService.create(user2.getId(), newRequest);

        // Действие
        BookingDto rejectedBooking = bookingService.approveBooking(newBooking.getId(), user1.getId(), Boolean.FALSE);

        // Проверка
        assertThat(rejectedBooking.getId(), equalTo(newBooking.getId()));
        assertThat(rejectedBooking.getStart(), equalTo(newBooking.getStart()));
        assertThat(rejectedBooking.getEnd(), equalTo(newBooking.getEnd()));
        assertThat(rejectedBooking.getItem(), notNullValue());
        assertThat(rejectedBooking.getItem().getClass(), equalTo(ItemDto.class));
        assertThat(rejectedBooking.getStatus(), equalTo(Statuses.REJECTED));
        assertThat(rejectedBooking.getBooker(), notNullValue());
        assertThat(rejectedBooking.getBooker().getClass(), equalTo(UserDto.class));
    }
}
*/
