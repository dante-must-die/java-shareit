package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.InvalidCommentException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserServiceImpl userService;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository,
                           UserServiceImpl userService,
                           CommentRepository commentRepository,
                           BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        User owner = userService.getUserEntityById(userId);

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);

        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Item item = getItemEntityById(itemId);

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
        Item item = getItemEntityById(itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);

        // Добавляем комментарии
        List<Comment> comments = commentRepository.findByItemId(itemId);
        itemDto.setComments(comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));

        // Добавляем информацию о бронированиях только для владельца
        if (item.getOwner().getId().equals(userId)) {
            Booking lastBooking = bookingRepository.findLastBooking(itemId, LocalDateTime.now());
            Booking nextBooking = bookingRepository.findNextBooking(itemId, LocalDateTime.now());

            itemDto.setLastBooking(lastBooking != null ? BookingMapper.toBookingDto(lastBooking) : null);
            itemDto.setNextBooking(nextBooking != null ? BookingMapper.toBookingDto(nextBooking) : null);
        }

        return itemDto;
    }

    public Item getItemEntityById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found"));
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        List<Item> items = itemRepository.findByOwnerId(userId);
        return items.stream()
                .map(ItemMapper::toItemDto)
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
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        // Проверяем, что пользователь существует
        User user = userService.getUserEntityById(userId);
        // Проверяем, что вещь существует
        Item item = getItemEntityById(itemId);
        // Проверяем, что пользователь бронировал эту вещь и бронирование завершено
        List<Booking> bookings = bookingRepository.findCompletedBookingsByBookerAndItem(userId, itemId, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new InvalidCommentException("User has not booked this item or booking is not completed");
        }
        // Создаём комментарий
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(savedComment);
    }
}
