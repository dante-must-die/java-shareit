package booking;

import ru.practicum.ShareItServerApplication;
import ru.practicum.booking.Booking;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.NewBookingRequest;
import ru.practicum.booking.dto.UpdateBookingRequest;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.enums.Statuses;
import ru.practicum.item.Item;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;
import ru.practicum.user.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ContextConfiguration(classes = ShareItServerApplication.class)
public class BookingMapperTest {
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime nextDay = now.plusDays(1);

    private final User user = new User(1L, "John Doe", "john.doe@mail.com");
    private final ItemRequest itemRequest = new ItemRequest(1L, "Need a hammer", user, LocalDateTime.now());
    private final Item item = new Item(1L, "name", "description", Boolean.TRUE, user, itemRequest);
    private final Booking booking = new Booking(1L, now, nextDay, item, Statuses.WAITING, user);
    private final UserDto userDto = new UserDto(1L, "John Doe", "john.doe@mail.com");
    private final ItemDto itemDto = new ItemDto(1L, "name", "description", Boolean.TRUE, itemRequest.getId());
    private final NewBookingRequest newBooking = new NewBookingRequest(now, nextDay, item.getId(), user.getId());
    private final UpdateBookingRequest updBooking = new UpdateBookingRequest(1L, now, nextDay, item.getId(), Statuses.WAITING, user.getId());
    private final UpdateBookingRequest updEmptyBooking = new UpdateBookingRequest(1L, null, null, item.getId(), Statuses.WAITING, user.getId());
    private final BookingDto dto = new BookingDto(1L, now, nextDay, itemDto, Statuses.WAITING, userDto);

    @Test
    public void toBookingDtoTest() {
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
        assertThat(bookingDto, equalTo(dto));
    }

    @Test
    public void toBookingTest() {
        Booking b = BookingMapper.toBooking(newBooking, user, item);
        assertThat(b.getStart(), equalTo(booking.getStart()));
        assertThat(b.getEnd(), equalTo(booking.getEnd()));
        assertThat(b.getStatus(), equalTo(booking.getStatus()));
        assertThat(b.getItem(), equalTo(item));
        assertThat(b.getBooker(), equalTo(user));
    }

    @Test
    public void updateBookingFieldsTest() {
        Booking b = BookingMapper.updateBookingFields(booking, updBooking);
        assertThat(b.getId(), equalTo(booking.getId()));
        assertThat(b.getStart(), equalTo(booking.getStart()));
        assertThat(b.getEnd(), equalTo(booking.getEnd()));
        assertThat(b.getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    public void updateBookingEmptyFieldsTest() {
        Booking b = BookingMapper.updateBookingFields(booking, updEmptyBooking);
        assertThat(b.getId(), equalTo(booking.getId()));
        assertThat(b.getStart(), equalTo(booking.getStart()));
        assertThat(b.getEnd(), equalTo(booking.getEnd()));
        assertThat(b.getStatus(), equalTo(booking.getStatus()));
    }
}
