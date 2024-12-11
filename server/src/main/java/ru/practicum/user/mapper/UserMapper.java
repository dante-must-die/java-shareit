package ru.practicum.user.mapper;


import ru.practicum.user.User;
import ru.practicum.user.dto.UserDto;

/**
 * Маппер для пользователя.
 */
public class UserMapper {

    public static UserDto toUserDto(User user) {
        if (user == null) return null;
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User toUser(UserDto userDto) {
        if (userDto == null) return null;
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}
