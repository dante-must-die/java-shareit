package YandexPracticium.item;

import YandexPracticium.request.ItemRequest;
import YandexPracticium.user.User;
import lombok.*;


import jakarta.persistence.*;

@Getter
@Setter
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
    private ItemRequest request;
}
