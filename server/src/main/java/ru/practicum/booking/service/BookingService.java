package ru.practicum.booking.service;

import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.NewBookingRequest;
import ru.practicum.booking.dto.UpdateBookingRequest;

import java.util.Collection;

/**
 * Сервис для управления бронированиями.
 */
public interface BookingService {
    BookingDto create(Long userId, NewBookingRequest request);

    BookingDto findBooking(Long bookingId, Long userId);

    Collection<BookingDto> findAllBookingsByUser(Long userId, String state);

    Collection<BookingDto> findAllBookingsByOwnerItems(Long userId, String state);

    BookingDto update(Long userId, UpdateBookingRequest request);

    void delete(Long bookingId);

    BookingDto approveBooking(Long bookingId, Long userId, Boolean approved);
}
