package ru.practicum.booking.dto;

import ru.practicum.enums.Statuses;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Запрос для обновления существующего бронирования.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class UpdateBookingRequest {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Statuses status;
    private Long bookerId;

    public boolean hasStart() {
        return this.start != null;
    }

    public boolean hasEnd() {
        return this.end != null;
    }
}
