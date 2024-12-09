package ru.practicum.booking.service;

import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.NewBookingRequest;
import ru.practicum.booking.dto.UpdateBookingRequest;
import ru.practicum.booking.mapper.BookingMapper;
import ru.practicum.enums.States;
import ru.practicum.enums.Statuses;
import ru.practicum.exception.AccessDeniedException;
import ru.practicum.exception.ItemNotAvailableException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.item.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookingServiceImpl implements BookingService {
    BookingRepository repository;
    UserRepository userRepository;
    ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository repository, UserRepository userRepository, ItemRepository itemRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    private Booking findById(Long bookingId) {
        return repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование c ID %d не найдено", bookingId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь создающий бронирование " +
                        "c ID %d не найден", userId)));
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь для бронирования c ID %d не найдена", itemId)));
    }

    @Override
    @Transactional
    public BookingDto create(Long userId, NewBookingRequest request) {
        log.debug("Создаем запись о бронировании");

        Item findItem = findItemById(request.getItemId());
        User findUser = findUserById(userId);

        if (!findItem.getAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования!");
        }

        if (findUser.getId().equals(findItem.getOwner().getId())) {
            throw new ValidationException("Нельзя бронировать собственную вещь");
        }

        Booking booking = BookingMapper.toBooking(request, findUser, findItem);
        booking = repository.save(booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto findBooking(Long bookingId, Long userId) {
        log.debug("Ищем бронирование с ID {}", bookingId);

        Booking booking = findById(bookingId);
        User owner = findUserById(booking.getItem().getOwner().getId());
        if (!booking.getBooker().getId().equals(userId) && !owner.getId().equals(userId)) {
            throw new ValidationException("Только владелец вещи и создатель брони могут просматривать данные о бронировании");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<BookingDto> findAllBookingsByUser(Long userId, String state) {
        States currentState = States.valueOf(state);
        User findUser = findUserById(userId);
        Collection<Booking> bookingList = new ArrayList<>();

        switch (currentState) {
            case ALL:
                bookingList = repository.findAllByBookerId(userId);
                log.debug("Получаем записи о всех бронированиях пользователя");
                break;
            case CURRENT:
                bookingList = repository.findAllCurrentBookingByBookerId(userId);
                log.debug("Получаем записи о всех текущих бронированиях пользователя");
                break;
            case PAST:
                bookingList = repository.findAllPastBookingByBookerId(userId);
                log.debug("Получаем записи о завершенных бронированиях пользователя");
                break;
            case FUTURE:
                bookingList = repository.findAllFutureBookingByBookerId(userId);
                log.debug("Получаем записи о будущих бронированиях пользователя");
                break;
            case WAITING:
                bookingList = repository.findAllByBookerIdAndStatus(userId, Statuses.WAITING);
                log.debug("Получаем записи бронирований ожидающих подтверждения пользователя");
                break;
            case REJECTED:
                bookingList = repository.findAllByBookerIdAndStatus(userId, Statuses.REJECTED);
                log.debug("Получаем записи об отклоненных бронированиях пользователя");
                break;
        }

        return bookingList.stream()
                .map(BookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<BookingDto> findAllBookingsByOwnerItems(Long userId, String state) {
        States currentState = States.valueOf(state);
        User findUser = findUserById(userId);
        Collection<Booking> bookingList = new ArrayList<>();

        switch (currentState) {
            case ALL:
                bookingList = repository.findAllByOwnerId(userId);
                log.debug("Получаем записи бронирований вещей пользователя");
                break;
            case CURRENT:
                bookingList = repository.findAllCurrentBookingByOwnerId(userId);
                log.debug("Получаем записи о всех текущих бронированиях вещей пользователя");
                break;
            case PAST:
                bookingList = repository.findAllPastBookingByOwnerId(userId);
                log.debug("Получаем записи о завершенных бронированиях вещей пользователя");
                break;
            case FUTURE:
                bookingList = repository.findAllFutureBookingByOwnerId(userId);
                log.debug("Получаем записи о будущих бронированиях вещей пользователя");
                break;
            case WAITING:
                bookingList = repository.findAllByOwnerIdAndStatus(userId, Statuses.WAITING);
                log.debug("Получаем записи о бронировании вещей пользователя ожидающих подтверждения");
                break;
            case REJECTED:
                bookingList = repository.findAllByOwnerIdAndStatus(userId, Statuses.REJECTED);
                log.debug("Получаем записи об отклоненных бронированиях вещей пользователя");
                break;
        }

        return bookingList.stream()
                .map(BookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingDto update(Long userId, UpdateBookingRequest request) {
        log.debug("Обновляем данные о бронировании");

        if (request.getId() == null) {
            throw new ValidationException("ID бронирования должен быть указан");
        }

        User owner = findUserById(userId);
        Booking findBooking = findById(request.getId());

        if (!findBooking.getBooker().getId().equals(userId) && !owner.getId().equals(userId)) {
            throw new ValidationException("Только владелец вещи и создатель брони могут редактировать данные о бронировании");
        }

        Booking updatedBooking = BookingMapper.updateBookingFields(findBooking, request);
        updatedBooking = repository.save(updatedBooking);

        return BookingMapper.toBookingDto(updatedBooking);
    }

    @Override
    @Transactional
    public void delete(Long bookingId) {
        Booking booking = findById(bookingId);
        log.debug("Удаляем данные о бронировании с ID {}", booking.getId());
        repository.delete(booking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = findById(bookingId);
        Item item = findItemById(booking.getItem().getId());

        if (!item.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Менять статус вещи может только её владелец");
        }

        if (!booking.getStatus().equals(Statuses.WAITING)) {
            throw new ItemNotAvailableException("Вещь уже занята!");
        }

        booking.setStatus(approved ? Statuses.APPROVED : Statuses.REJECTED);
        return BookingMapper.toBookingDto(booking);
    }
}