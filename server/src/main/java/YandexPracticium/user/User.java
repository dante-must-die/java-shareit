package YandexPracticium.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class    User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // Уникальный идентификатор пользователя

    @Column(nullable = false)
    private String name;    // Имя или логин пользователя

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;   // Адрес электронной почты
}
