package ru.practicum.booking.dto;

import ru.practicum.enums.Statuses;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateBookingRequest {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long itemId;
    Statuses status;
    Long bookerId;

    public boolean hasStart() {
        return this.start != null;
    }

    public boolean hasEnd() {
        return this.end != null;
    }
}
