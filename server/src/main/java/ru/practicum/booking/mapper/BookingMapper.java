package ru.practicum.booking.mapper;


import ru.practicum.booking.Booking;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.NewBookingRequest;
import ru.practicum.booking.dto.UpdateBookingRequest;
import ru.practicum.enums.Statuses;
import ru.practicum.item.Item;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.user.User;
import ru.practicum.user.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Маппер для преобразования между сущностью Booking и DTO.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setItem(ItemMapper.toItemDto(booking.getItem()));
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        dto.setBooker(UserMapper.toUserDto(booking.getBooker()));

        return dto;
    }

    public static Booking toBooking(NewBookingRequest request, User booker, Item item) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStart(request.getStart());
        booking.setEnd(request.getEnd());
        booking.setStatus(Statuses.WAITING);
        booking.setBooker(booker);

        return booking;
    }

    public static Booking updateBookingFields(Booking booking, UpdateBookingRequest request) {
        if (request.hasStart()) {
            booking.setStart(request.getStart());
        }

        if (request.hasEnd()) {
            booking.setEnd(request.getEnd());
        }

        booking.setStatus(request.getStatus());

        return booking;
    }
}
