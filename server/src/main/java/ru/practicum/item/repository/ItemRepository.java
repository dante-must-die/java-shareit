package ru.practicum.item.repository;

import ru.practicum.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий для управления предметами.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);

    @Query("SELECT i FROM Item i WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%'))) AND i.available = TRUE")
    List<Item> search(String text);

    List<Item> findByRequestId(Long requestId);

    List<Item> findByRequestIdIn(List<Long> requestIds);
}
