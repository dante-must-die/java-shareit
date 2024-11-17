package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // Уникальный идентификатор вещи

    @Column(nullable = false)
    private String name;                // Краткое название

    @Column(nullable = false)
    private String description;         // Развёрнутое описание

    @Column(name = "is_available", nullable = false)
    private Boolean available;          // Статус о том, доступна или нет вещь для аренды

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;                 // Владелец вещи

    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;        // Запрос, к которому относится вещь (может быть null)
}
