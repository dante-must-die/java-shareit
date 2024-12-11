package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

/**
 * Запрос для создания нового пользователя.
 */
@Data
public class NewUserRequest {
    @NotBlank(message = "E-mail должен быть указан")
    @Email(message = "Email должен быть в формате user@yandex.ru")
    private String email;

    @NotBlank(message = "Имя пользователя должно быть указано")
    private String name;

    @PastOrPresent(message = "Дата рождения не может быть больше текущего дня")
    private LocalDate birthday;
}
