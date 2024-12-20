package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для класса User
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;            // Уникальный идентификатор пользователя
    private String name;        // Имя или логин пользователя
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email should be valid")
    private String email;       // Адрес электронной почты
}
