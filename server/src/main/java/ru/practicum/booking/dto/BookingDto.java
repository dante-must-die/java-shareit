package ru.practicum.booking.dto;

import ru.practicum.enums.Statuses;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

/**
 * Объект передачи данных для бронирования.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private Statuses status;
    private UserDto booker;
}