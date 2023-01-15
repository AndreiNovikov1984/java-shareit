package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getUsers() {                 // метод получения списка пользователей
        log.info("Получение списка всех пользователей");
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable long userId) {         // метод получения пользователя по Id
        log.info("Получение пользователя с userId={}", userId);
        return new ResponseEntity<>(userService.getUserWithId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> postUser(@RequestBody UserDto userDto) {  // метод добавления пользователя
        log.info("Создание пользователя {}", userDto);
        return new ResponseEntity<>(userService.postUser(userDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}")                                  // метод обновления пользователя
    public ResponseEntity<UserDto> patchUser(@PathVariable long userId,
                                             @RequestBody UserDto userDto) {
        log.info("Обновление пользователя с userId={}", userId);
        return new ResponseEntity<>(userService.patchUser(userId, userDto), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")                                     // метод удаления пользователя
    public void deleteUser(@PathVariable long userId) {
        log.info("Удаление пользователя с userId={}", userId);
        userService.deleteUser(userId);
    }
}
