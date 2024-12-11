package ru.practicum.booking.dto;

import ru.practicum.enums.Statuses;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.time.LocalDateTime;

/**
 * DTO для обновления существующего бронирования.
 */
@Data
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
