package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getUsers() {                 // метод получения списка пользователей
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) {         // метод получения пользователя по Id
        return userService.getUserWithId(id);
    }

    @PostMapping
    public UserDto postUser(@RequestBody UserDto userDto) {  // метод добавления пользователя
        return userService.postUser(userDto);
    }

    @PatchMapping("/{id}")                                  // метод обновления пользователя
    public UserDto patchUser(@PathVariable long id, @RequestBody UserDto userDto) {
        return userService.patchUser(id, userDto);
    }

    @DeleteMapping("/{id}")                                     // метод удаления пользователя
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
