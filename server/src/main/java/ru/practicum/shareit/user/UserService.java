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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Collection<UserDto> getUsers() {             // метод получения списка пользователей
        return userRepository.findAll().stream()
                .map(userMapper::convertUserToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserWithId(long userId) {         // метод получения пользователя по Id
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return userMapper.convertUserToDto(user.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id пользователя. Попробуйте еще раз.");
        }
    }

    public UserDto postUser(UserDto userDto) {          // метод добавления пользователя
        User user = userMapper.convertDtoToUser(userDto);
        Validation.validationUser(user);
        return userMapper.convertUserToDto(userRepository.save(user));
    }

    public UserDto patchUser(long userId, UserDto userDto) {        // метод обновления пользователя
        User user = userMapper.convertDtoToUser(userDto);
        user.setId(userId);
        Optional<User> userToUpdate = userRepository.findById(userId);
        if (user.getName() == null) {
            user.setName(userToUpdate.get().getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userToUpdate.get().getEmail());
        }
        Validation.validationUser(user);
        return userMapper.convertUserToDto(userRepository.save(user));
    }

    public void deleteUser(long userId) {                       // метод удаления пользователя
        userRepository.deleteById(userId);
    }
}
