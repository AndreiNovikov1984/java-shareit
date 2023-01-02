package ru.practicum.shareit.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

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

    public static void validationBooking(BookingDto booking) {
        if ((booking.getStart().isAfter(booking.getEnd())) || (booking.getStart().isBefore(LocalDateTime.now()))
                || (booking.getEnd().isBefore(LocalDateTime.now()))) {
            log.warn("Ошибка в email - {}", booking.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Данные бронирования не корректны. Попробуйте еще раз.");
        }
    }

    public static void validationComment(Comment comment) {
        if ((comment == null) || (comment.getText().equals("null")) ||
                (comment.getText().equals(""))) {
            log.warn("Ошибка в комменте - {}", comment.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Переданный комментарий пустой");
        }
    }

    public static void validationItemRequest(ItemRequestDto itemRequestDto) {
        if ((itemRequestDto == null) || (itemRequestDto.getDescription() == null) ||
                (itemRequestDto.getDescription().equals("null")) || (itemRequestDto.getDescription().equals(""))) {
            log.warn("Ошибка в запросе - {}", itemRequestDto.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Переданный запрос пустой");
        }
    }
}
