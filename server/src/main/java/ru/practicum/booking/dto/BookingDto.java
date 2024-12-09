package ru.practicum.booking.dto;

import ru.practicum.enums.Statuses;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    ItemDto item;
    Statuses status;
    UserDto booker;
}