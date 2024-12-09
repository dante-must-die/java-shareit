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
import YandexPracticium.request.ItemRequest;
import YandexPracticium.request.ItemRequestRepository;
import YandexPracticium.user.User;
import YandexPracticium.user.dto.UserDto;
import YandexPracticium.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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
    private final ItemRequestRepository itemRequestRepository;

    private User user1;
    private User user2;
    private Item item1;
    private ItemRequest request;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setName("Ivan Ivanov");
        user1.setEmail("ivan@email");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setName("Petr Petrov");
        user2.setEmail("petr@email");
        user2 = userRepository.save(user2);

        // Создаём запрос
        request = new ItemRequest();
        request.setDescription("Need a tool");
        request.setRequester(user1);
        request.setCreated(LocalDateTime.now());
        request = itemRequestRepository.save(request);

        item1 = new Item();
        item1.setName("name");
        item1.setDescription("description");
        item1.setAvailable(true);
        item1.setOwner(user1);
        item1.setRequest(request);
        item1 = itemRepository.save(item1);
    }

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
        NewBookingRequest newRequest = new NewBookingRequest(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item1.getId(),
                user2.getId()
        );

        BookingDto booking = bookingService.create(user2.getId(), newRequest);

        MatcherAssert.assertThat(booking.getId(), notNullValue());
        MatcherAssert.assertThat(booking.getStatus(), equalTo(Statuses.WAITING));
        MatcherAssert.assertThat(booking.getBooker(), notNullValue());
        MatcherAssert.assertThat(booking.getItem(), notNullValue());
        MatcherAssert.assertThat(booking.getItem().getClass(), equalTo(ItemDto.class));
        MatcherAssert.assertThat(booking.getBooker().getClass(), equalTo(UserDto.class));
    }

    @Test
    void getBookingTest() {
        Booking bookingEntity = createBooking(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                Statuses.APPROVED,
                user1,
                item1
        );

        BookingDto loadedBooking = bookingService.findBooking(bookingEntity.getId(), user1.getId());

        MatcherAssert.assertThat(loadedBooking.getId(), notNullValue());
        MatcherAssert.assertThat(loadedBooking.getStatus(), equalTo(Statuses.APPROVED));
        MatcherAssert.assertThat(loadedBooking.getItem(), notNullValue());
        MatcherAssert.assertThat(loadedBooking.getBooker(), notNullValue());
    }

    @Test
    void findAllBookingsByUserAndStateAllTest() {
        createBooking(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                Statuses.APPROVED,
                user1,
                item1
        );

        Collection<BookingDto> loadBookings = bookingService.findAllBookingsByUser(user1.getId(), String.valueOf(States.ALL));
        MatcherAssert.assertThat(loadBookings, hasSize(1));
    }

    @Test
    void updateBookingTest() {
        Booking bookingEntity = createBooking(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                Statuses.APPROVED,
                user1,
                item1
        );

        UpdateBookingRequest updateRequest = new UpdateBookingRequest(
                bookingEntity.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item1.getId(),
                Statuses.REJECTED,
                user1.getId()
        );

        BookingDto updatedBooking = bookingService.update(user1.getId(), updateRequest);

        MatcherAssert.assertThat(updatedBooking.getStatus(), equalTo(Statuses.REJECTED));
        MatcherAssert.assertThat(updatedBooking.getItem().getId(), equalTo(item1.getId()));
        MatcherAssert.assertThat(updatedBooking.getBooker().getId(), equalTo(user1.getId()));
    }

    @Test
    void deleteItemTest() {
        Booking bookingEntity = createBooking(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                Statuses.APPROVED,
                user1,
                item1
        );

        bookingService.delete(bookingEntity.getId());

        List<Booking> bookings = bookingRepository.findById(bookingEntity.getId()).stream().toList();
        MatcherAssert.assertThat(bookings, empty());
    }

    @Test
    void approveBookingTest() {
        NewBookingRequest newRequest = new NewBookingRequest(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                item1.getId(),
                user2.getId()
        );
        BookingDto newBooking = bookingService.create(user2.getId(), newRequest);

        BookingDto approvedBooking = bookingService.approveBooking(newBooking.getId(), user1.getId(), Boolean.TRUE);
        MatcherAssert.assertThat(approvedBooking.getStatus(), equalTo(Statuses.APPROVED));
    }

    @Test
    void notApproveBookingTest() {
        NewBookingRequest newRequest = new NewBookingRequest(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                item1.getId(),
                user2.getId()
        );
        BookingDto newBooking = bookingService.create(user2.getId(), newRequest);
        BookingDto rejectedBooking = bookingService.approveBooking(newBooking.getId(), user1.getId(), Boolean.FALSE);

        MatcherAssert.assertThat(rejectedBooking.getStatus(), equalTo(Statuses.REJECTED));
    }
}
