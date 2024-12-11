package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для создания нового запроса на предмет.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewRequest {
    private String description;

    private Long requestorId;
}
