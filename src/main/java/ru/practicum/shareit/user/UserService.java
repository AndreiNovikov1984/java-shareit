package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.support.Validation;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    public Collection<UserDto> getUsers() {             // метод получения списка пользователей
        return userStorage.getAllUsers().stream()
                .map(userMapper::convertUserToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserWithId(long userId) {         // метод получения пользователя по Id
        checkId(userId);
        return userMapper.convertUserToDto(userStorage.getUserById(userId));
    }

    public UserDto postUser(UserDto userDto) {          // метод добавления пользователя
        User user = userMapper.convertDtoToUser(userDto);
        Validation.validationUser(user);
        return userMapper.convertUserToDto(userStorage.addUser(user));
    }

    public UserDto patchUser(long userId, UserDto userDto) {        // метод обновления пользователя
        checkId(userId);
        User user = userMapper.convertDtoToUser(userDto);
        user.setId(userId);
        return userMapper.convertUserToDto(userStorage.updateUser(user));
    }

    public User deleteUser(long userId) {                       // метод удаления пользователя
        checkId(userId);
       return userStorage.deleteUserById(userId);
    }

    private void checkId(long userId) {
        if ((userId < 0) || (userId == 0)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id. Попробуйте еще раз.");
        }
    }
}
