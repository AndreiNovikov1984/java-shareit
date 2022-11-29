package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> userMap = new HashMap<>();
    private static long identificator = 0;


    public Collection<User> getAllUsers() {                         // метод получения списка пользователей
        log.info("Информация о пользователях передана");
        return userMap.values();
    }

    public User getUserById(long userId) {                      // метод получения пользователя по Id
        log.info("Информация о пользователe ID = {} передана", userId);
        return userMap.values().stream()
                .filter(p -> p.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id. Попробуйте еще раз."));
    }

    public User addUser(User user) {                            // метод добавления пользователя
        userMap.values().forEach(x -> {
            if (x.getEmail().equals(user.getEmail()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Данный e-mail уже занят. Попробуйте другой");
        });

        if (!userMap.containsKey(user.getId())) {
            identificator++;
            user.setId(identificator);
        }
        userMap.put(user.getId(), user);
        log.info("Пользователь ID = {} добавлен", user.getId());
        return user;
    }

    public User updateUser(User user) {                         // метод обновления пользователя
        if (user.getName() == null) {
            user.setName(userMap.get(user.getId()).getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userMap.get(user.getId()).getEmail());
        }

        List<User> userList = userMap.values().stream()
                .filter(p -> p.getEmail().equals(user.getEmail()))
                .collect(Collectors.toList());
        if (userList.size() != 0) {
            if (userList.get(0).getId() != user.getId()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Данный e-mail уже занят. Попробуйте другой");
            }
        }

        userMap.put(user.getId(), user);
        log.info("Пользователь ID = {} обновлен", user.getId());
        return user;
    }

    public User deleteUserById(long userId) {               // метод удаления пользователя
            log.info("Пользователь ID = {} удален", userId);
            return userMap.remove(userId);
    }
}
