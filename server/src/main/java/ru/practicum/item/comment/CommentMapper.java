package ru.practicum.item.comment;

import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.time.LocalDateTime;

/**
 * Утилитный класс для преобразования объектов {@link Comment} и {@link CommentDto}.
 * Предоставляет методы для конвертации между сущностью комментария и ее DTO.
 */

public class CommentMapper {

    private CommentMapper() {

    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Comment mapToComment(User author, Item item, CommentDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }
}
