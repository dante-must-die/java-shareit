/*
package booking;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.booking.Booking;
import YandexPracticium.booking.dto.BookingDto;
import YandexPracticium.booking.dto.NewBookingRequest;
import YandexPracticium.booking.dto.UpdateBookingRequest;
import YandexPracticium.booking.mapper.BookingMapper;
import YandexPracticium.enums.Statuses;
import YandexPracticium.item.Item;
import YandexPracticium.item.dto.ItemDto;
import YandexPracticium.user.User;
import YandexPracticium.user.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;


import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ContextConfiguration(classes = ShareItServerApplication.class)

public class BookingMapperTest {
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime nextDay = LocalDateTime.now().plusDays(1);

    private final User user = new User(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));
    private final Item item = new Item(1L, "name", "description", Boolean.TRUE, user, 1L);
    private final Booking booking = new Booking(1L, now, nextDay, item, Statuses.WAITING, user);
    private final UserDto userDto = new UserDto(1L, "john.doe@mail.com", "John Doe", LocalDate.of(2022, 7, 3));
    private final ItemDto itemDto = new ItemDto(1L, "name", "description", Boolean.TRUE, 1L, 1L);
    private final NewBookingRequest newBooking = new NewBookingRequest(now, nextDay, 1L, 1L);
    private final UpdateBookingRequest updBooking = new UpdateBookingRequest(1L, now, nextDay, 1L, Statuses.WAITING, 1L);
    private final UpdateBookingRequest updEmptyBooking = new UpdateBookingRequest(1L, null, null, 1L, Statuses.WAITING, 1L);
    private final BookingDto dto = new BookingDto(1L, now, nextDay, itemDto, Statuses.WAITING, userDto);

    @Test
    public void toBookingDtoTest() {
        BookingDto bookingDto = BookingMapper.mapToBookingDto(booking);
        assertThat(bookingDto, equalTo(dto));
    }

    @Test
    public void toBookingTest() {
        Booking b = BookingMapper.mapToBooking(newBooking, user, item);
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
*/
