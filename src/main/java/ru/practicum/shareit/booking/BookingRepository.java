package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.Booking.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    // Получить бронирования для владельца вещей
    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 ORDER BY b.start DESC")
    List<Booking> findByOwnerId(Long ownerId);

    // Бронирования по статусу для пользователя
    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status status);

    // Текущие бронирования пользователя
    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId, LocalDateTime now1, LocalDateTime now2);

    // Прошлые бронирования пользователя
    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime now);

    // Будущие бронирования пользователя
    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime now);

    // Аналогичные методы для владельца
    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, Status status);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId, LocalDateTime now1, LocalDateTime now2);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start < :now ORDER BY b.end DESC")
    Booking findLastBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start > :now ORDER BY b.start ASC")
    Booking findNextBooking(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.item.id = :itemId AND b.end < :now AND b.status = 'APPROVED'")
    List<Booking> findCompletedBookingsByBookerAndItem(@Param("userId") Long userId, @Param("itemId") Long itemId, @Param("now") LocalDateTime now);

}
