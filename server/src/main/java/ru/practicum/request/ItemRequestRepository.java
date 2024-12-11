package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий для управления запросами на предметы.
 */
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequesterIdOrderByCreatedDesc(Long requesterId);

    List<ItemRequest> findByRequesterIdNotOrderByCreatedDesc(Long requesterId);
}
