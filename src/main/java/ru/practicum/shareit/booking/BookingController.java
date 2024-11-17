package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Добавление нового бронирования
    @PostMapping
    public BookingDto createBooking(@RequestHeader(USER_HEADER) Long userId,
                                    @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(userId, bookingDto);
    }

    // Подтверждение или отклонение бронирования
    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(USER_HEADER) Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam("approved") boolean approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    // Получение бронирования по ID
    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(USER_HEADER) Long userId,
                                     @PathVariable Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    // Получение списка бронирований текущего пользователя
    @GetMapping
    public List<BookingDto> getBookingsByUser(@RequestHeader(USER_HEADER) Long userId,
                                              @RequestParam(value = "state", defaultValue = "ALL") String state) {
        return bookingService.getBookingsByUser(userId, state);
    }

    // Получение списка бронирований для всех вещей текущего пользователя
    @GetMapping("/owner")
    public List<BookingDto> getBookingsForOwner(@RequestHeader(USER_HEADER) Long userId,
                                                @RequestParam(value = "state", defaultValue = "ALL") String state) {
        return bookingService.getBookingsForOwner(userId, state);
    }
}
