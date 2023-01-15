package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoGate;

import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserControllerGate {
    private final UserClient userClient;

    @GetMapping                                                 // метод получения списка пользователей
    public ResponseEntity<Object> getUsers() {
        log.info("Get users");
        return userClient.getUsers();
    }

    @GetMapping("/{userId}")                                    // метод получения пользователя по Id
    public ResponseEntity<Object> getUser(@Positive @PathVariable long userId) {
        log.info("Get user with userId={}", userId);
        return userClient.getUser(userId);
    }

    @PostMapping                            // метод добавления пользователя
    public ResponseEntity<Object> postUser(@RequestBody UserDtoGate userDto) {
        log.info("Creating user {}", userDto);
        return userClient.postUser(userDto);
    }

    @PatchMapping("/{userId}")                                  // метод обновления пользователя
    public ResponseEntity<Object> patchUser(@Positive @PathVariable long userId,
                                            @RequestBody UserDtoGate userDto) {
        log.info("Patching userId={}", userId);
        return userClient.patchUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")                                     // метод удаления пользователя
    public ResponseEntity<Object> deleteUser(@Positive @PathVariable long userId) {
        log.info("Deleting userId={}", userId);
        return userClient.deleteUser(userId);
    }
}
