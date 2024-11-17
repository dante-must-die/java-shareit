package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Уникальный идентификатор запроса

    @Column(nullable = false)
    private String description; // Текст запроса, содержащий описание требуемой вещи

    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor; // Пользователь, создавший запрос

    @Column(nullable = false)
    private LocalDateTime created;
}
