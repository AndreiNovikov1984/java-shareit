package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
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
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getUsers() {                 // метод получения списка пользователей
        Collection<UserDto> users = userService.getUsers();
        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable long id) {         // метод получения пользователя по Id
        return new ResponseEntity<>(userService.getUserWithId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> postUser(@RequestBody UserDto userDto) {  // метод добавления пользователя
        return new ResponseEntity<>(userService.postUser(userDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")                                  // метод обновления пользователя
    public ResponseEntity<UserDto> patchUser(@PathVariable long id, @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.patchUser(id, userDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")                                     // метод удаления пользователя
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        return userService.deleteUser(id) != null
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
