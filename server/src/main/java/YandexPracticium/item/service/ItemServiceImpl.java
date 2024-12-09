package YandexPracticium.item.service;

import YandexPracticium.booking.Booking;
import YandexPracticium.booking.BookingRepository;
import YandexPracticium.booking.mapper.BookingMapper;
import YandexPracticium.enums.Statuses;
import YandexPracticium.exception.AccessDeniedException;
import YandexPracticium.exception.NotFoundException;
import YandexPracticium.exception.ValidationException;
import YandexPracticium.item.Item;
import YandexPracticium.item.comment.Comment;
import YandexPracticium.item.comment.CommentDto;
import YandexPracticium.item.comment.CommentMapper;
import YandexPracticium.item.comment.CommentRepository;
import YandexPracticium.item.dto.ItemDto;
import YandexPracticium.item.mapper.ItemMapper;
import YandexPracticium.item.repository.ItemRepository;
import YandexPracticium.request.ItemRequest;
import YandexPracticium.request.ItemRequestRepository;
import YandexPracticium.user.User;
import YandexPracticium.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           CommentRepository commentRepository,
                           BookingRepository bookingRepository,
                           ItemRequestRepository itemRequestRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    private Item findById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь c ID %d не найдена", itemId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %d не найден", userId)));
    }

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User owner = findUserById(userId);

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);

        if (itemDto.getRequestId() != null) {
            ItemRequest request = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NoSuchElementException("Request not found"));
            item.setRequest(request);
        }

        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item item = findById(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Only owner can update the item");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = findById(itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);

        // Добавляем комменты
        List<Comment> comments = commentRepository.findByItemId(itemId);
        itemDto.setComments(comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));

        // Добавляем информацию о бронированиях только для владельца
        if (item.getOwner().getId().equals(userId)) {
            List<Booking> lastBookings = bookingRepository.findLastBookings(itemId, Statuses.APPROVED, LocalDateTime.now());
            List<Booking> nextBookings = bookingRepository.findNextBookings(itemId, Statuses.APPROVED, LocalDateTime.now());

            itemDto.setLastBooking(lastBookings.isEmpty() ? null : BookingMapper.toBookingDto(lastBookings.get(0)));
            itemDto.setNextBooking(nextBookings.isEmpty() ? null : BookingMapper.toBookingDto(nextBookings.get(0)));
        }

        return itemDto;
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        List<Item> items = itemRepository.findByOwnerId(userId);

        return items.stream()
                .map(item -> {
                    ItemDto itemDto = ItemMapper.toItemDto(item);

                    // Добавляем комментарии
                    List<Comment> comments = commentRepository.findByItemId(item.getId());
                    itemDto.setComments(comments.stream()
                            .map(CommentMapper::toCommentDto)
                            .collect(Collectors.toList()));

                    // Добавляем информацию о бронированиях
                    List<Booking> lastBookings = bookingRepository.findLastBookings(item.getId(), Statuses.APPROVED, LocalDateTime.now());
                    List<Booking> nextBookings = bookingRepository.findNextBookings(item.getId(), Statuses.APPROVED, LocalDateTime.now());

                    itemDto.setLastBooking(lastBookings.isEmpty() ? null : BookingMapper.toBookingDto(lastBookings.get(0)));
                    itemDto.setNextBooking(nextBookings.isEmpty() ? null : BookingMapper.toBookingDto(nextBookings.get(0)));

                    return itemDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        List<Item> items = itemRepository.search(text);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User findUser = findUserById(userId);
        Item findItem = findById(itemId);

        LocalDateTime now = LocalDateTime.now().plusHours(5);
        boolean canComment = bookingRepository.existsByBookerIdAndItemIdAndEndBefore(userId, itemId, now);
        if (!canComment) {
            throw new ValidationException(String.format("Пользователь %s не может оставить комментарий, " +
                    "так как не пользовался вещью %s", findUser.getName(), findItem.getName()));
        }

        Comment comment = CommentMapper.mapToComment(findUser, findItem, commentDto);
        comment = commentRepository.save(comment);

        return CommentMapper.toCommentDto(comment);
    }
}
