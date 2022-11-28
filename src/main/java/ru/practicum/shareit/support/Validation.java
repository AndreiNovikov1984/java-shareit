package ru.practicum.shareit.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Service
@Slf4j
public class Validation {
    public static void validationUser(User user) {
        if ((user.getEmail() == null) || (user.getEmail().equals("")) || (user.getEmail().equals("null")) ||
                (!user.getEmail().contains("@"))) {
            log.warn("Ошибка в email - {}", user.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail не корректный. Попробуйте еще раз.");
        }
        if ((user.getName() == null) || (user.getName().equals("null")) ||
                (user.getName().equals(""))) {
            log.warn("Ошибка в имени пользователя - {}", user.getName());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имя пользователя не может быть пустым или содержать пробелы.");
        }
        if (user.getId() < 0) {
            log.warn("Некорректный id пользователя в запросе - {}", user.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id. Попробуйте еще раз.");
        }
    }

    public static void validationItem(Item item) {
        if ((item.getName() == null) || (item.getName().equals("null")) ||
                (item.getName().equals(""))) {
            log.warn("Ошибка в имени вещи - {}", item.getName());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имя вещи не может быть пустым.");
        }
        if ((item.getDescription() == null) || (item.getDescription().equals("null")) ||
                (item.getDescription().equals(""))) {
            log.warn("Ошибка в описании вещи - {}", item.getName());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Описание вещи не может быть пустым.");
        }
        if (item.getAvailable() == null) {
            log.warn("Ошибка в поле доступности вещи - {}", item.getName());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Поле доступности должно быть заполнено.");
        }
        if (item.getId() < 0) {
            log.warn("Некорректный id вещи в запросе - {}", item.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id. Попробуйте еще раз.");
        }
    }
}
