package ru.practicum.shareit.support;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"file:src/test/resources/schema.sql",
        "file:src/test/resources/test.sql"})
public class ValidationTest {
    private ResponseStatusException exeption;

    @Test
    void validationUserWithoutEmailTest() {
        User user = User.builder()
                .name("Ivan")
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationUser(user));
        Assertions.assertEquals("400 BAD_REQUEST \"E-mail не корректный. Попробуйте еще раз.\"", exeption.getMessage());
    }

    @Test
    void validationUserWithEmailEmptyTest() {
        User user = User.builder()
                .name("Ivan")
                .email("")
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationUser(user));
        Assertions.assertEquals("400 BAD_REQUEST \"E-mail не корректный. Попробуйте еще раз.\"", exeption.getMessage());
    }

    @Test
    void validationUserWithEmailNullTest() {
        User user = User.builder()
                .name("Ivan")
                .email("null")
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationUser(user));
        Assertions.assertEquals("400 BAD_REQUEST \"E-mail не корректный. Попробуйте еще раз.\"", exeption.getMessage());
    }

    @Test
    void validationUserWithIncorrectEmailTest() {
        User user = User.builder()
                .name("Ivan")
                .email("vanAya.com")
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationUser(user));
        Assertions.assertEquals("400 BAD_REQUEST \"E-mail не корректный. Попробуйте еще раз.\"", exeption.getMessage());
    }

    @Test
    void validationUserWithoutNameTest() {
        User user = User.builder()
                .email("van@ya.com")
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationUser(user));
        Assertions.assertEquals("400 BAD_REQUEST \"Имя пользователя не может быть пустым или содержать пробелы.\"", exeption.getMessage());
    }

    @Test
    void validationUserWithNameEmptyTest() {
        User user = User.builder()
                .name("")
                .email("van@ya.com")
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationUser(user));
        Assertions.assertEquals("400 BAD_REQUEST \"Имя пользователя не может быть пустым или содержать пробелы.\"", exeption.getMessage());
    }

    @Test
    void validationUserWithNameNullTest() {
        User user = User.builder()
                .name("null")
                .email("van@ya.com")
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationUser(user));
        Assertions.assertEquals("400 BAD_REQUEST \"Имя пользователя не может быть пустым или содержать пробелы.\"", exeption.getMessage());
    }

    @Test
    void validationItemWithoutNameTest() {
        Item item = Item.builder()
                .description("Thing4 description")
                .available(true)
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationItem(item));
        Assertions.assertEquals("400 BAD_REQUEST \"Имя вещи не может быть пустым.\"", exeption.getMessage());
    }

    @Test
    void validationItemWithNameNullTest() {
        Item item = Item.builder()
                .name("null")
                .description("Thing4 description")
                .available(true)
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationItem(item));
        Assertions.assertEquals("400 BAD_REQUEST \"Имя вещи не может быть пустым.\"", exeption.getMessage());
    }

    @Test
    void validationItemWithNameEmptyTest() {
        Item item = Item.builder()
                .name("")
                .description("Thing4 description")
                .available(true)
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationItem(item));
        Assertions.assertEquals("400 BAD_REQUEST \"Имя вещи не может быть пустым.\"", exeption.getMessage());
    }

    @Test
    void validationItemWithoutDescriptionTest() {
        Item item = Item.builder()
                .name("Thing4")
                .available(true)
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationItem(item));
        Assertions.assertEquals("400 BAD_REQUEST \"Описание вещи не может быть пустым.\"", exeption.getMessage());
    }

    @Test
    void validationItemWithDescriptionNullTest() {
        Item item = Item.builder()
                .name("Thing4")
                .description("null")
                .available(true)
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationItem(item));
        Assertions.assertEquals("400 BAD_REQUEST \"Описание вещи не может быть пустым.\"", exeption.getMessage());
    }

    @Test
    void validationItemWithDescriptionEmptyTest() {
        Item item = Item.builder()
                .name("Thing4")
                .description("")
                .available(true)
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationItem(item));
        Assertions.assertEquals("400 BAD_REQUEST \"Описание вещи не может быть пустым.\"", exeption.getMessage());
    }

    @Test
    void validationItemWithIncorrectAvailableTest() {
        Item item = Item.builder()
                .name("Thing4")
                .description("Thing4 description")
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationItem(item));
        Assertions.assertEquals("400 BAD_REQUEST \"Поле доступности должно быть заполнено.\"", exeption.getMessage());
    }

    @Test
    void validationBookingEndBeforeStartTest() {
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().plusDays(3))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1)
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationBooking(bookingDto));
        Assertions.assertEquals("400 BAD_REQUEST \"Данные бронирования не корректны. Попробуйте еще раз.\"", exeption.getMessage());
    }

    @Test
    void validationBookingIncorrectStartTest() {
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1)
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationBooking(bookingDto));
        Assertions.assertEquals("400 BAD_REQUEST \"Данные бронирования не корректны. Попробуйте еще раз.\"", exeption.getMessage());
    }

    @Test
    void validationBookingIncorrectEndTest() {
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().minusDays(1))
                .itemId(1)
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationBooking(bookingDto));
        Assertions.assertEquals("400 BAD_REQUEST \"Данные бронирования не корректны. Попробуйте еще раз.\"", exeption.getMessage());
    }

    @Test
    void validationCommentWithoutTextTest() {
        Comment comment = Comment.builder()
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationComment(comment));
        Assertions.assertEquals("400 BAD_REQUEST \"Переданный комментарий пустой\"", exeption.getMessage());
    }

    @Test
    void validationCommentWithTextNullTest() {
        Comment comment = Comment.builder()
                .text("null")
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationComment(comment));
        Assertions.assertEquals("400 BAD_REQUEST \"Переданный комментарий пустой\"", exeption.getMessage());
    }

    @Test
    void validationCommentWithTextEmptyTest() {
        Comment comment = Comment.builder()
                .text("")
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationComment(comment));
        Assertions.assertEquals("400 BAD_REQUEST \"Переданный комментарий пустой\"", exeption.getMessage());
    }

    @Test
    void validationItemRequestWithoutDescriptionTest() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationItemRequest(itemRequestDto));
        Assertions.assertEquals("400 BAD_REQUEST \"Переданный запрос пустой\"", exeption.getMessage());
    }

    @Test
    void validationItemRequestWithDescriptionEmptyTest() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("")
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationItemRequest(itemRequestDto));
        Assertions.assertEquals("400 BAD_REQUEST \"Переданный запрос пустой\"", exeption.getMessage());
    }

    @Test
    void validationItemRequestWithDescriptionNullTest() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("null")
                .build();
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                Validation.validationItemRequest(itemRequestDto));
        Assertions.assertEquals("400 BAD_REQUEST \"Переданный запрос пустой\"", exeption.getMessage());
    }

}
