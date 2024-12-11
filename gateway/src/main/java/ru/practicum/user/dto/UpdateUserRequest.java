package ru.practicum.user.dto;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Запрос для обновления информации о пользователе.
 */
@Data
@EqualsAndHashCode(of = {"id"})
public class UpdateUserRequest {
    private Long id;
    private String email;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть больше текущего дня")
    private LocalDate birthday;

    public boolean hasEmail() {
        return !StringUtils.isBlank(this.email);
    }

    public boolean hasName() {
        return !StringUtils.isBlank(this.name);
    }

    public boolean hasBirthday() {
        return this.birthday != null;
    }
}
