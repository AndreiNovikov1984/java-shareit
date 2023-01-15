package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"file:../server/src/test/resources/schema.sql",
        "file:../server/src/test/resources/test.sql"})
public class BookingServiceTest {
    @Autowired
    private BookingService bookingService;
    private ResponseStatusException exeption;

    @Test
    void getBookingsIncorrectIdStatus() {
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                bookingService.getBooking(1, 100));
        Assertions.assertEquals("404 NOT_FOUND \"Такого бронирования не существует.\"", exeption.getMessage());
    }

    @Test
    void getBookingsIncorrectUserIdStatus() {
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                bookingService.getBooking(3, 1));
        Assertions.assertEquals("404 NOT_FOUND \"Пользователь не является владельцем/забронировавшим данную вещь.\"", exeption.getMessage());
    }
}
