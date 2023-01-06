package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.ItemService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"file:src/test/resources/schema.sql",
        "file:src/test/resources/test.sql"})
public class ItemServiceTest {
    @Autowired
    private ItemService itemService;
    private ResponseStatusException exeption;

    @Test
    void getItemTest() {
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                itemService.getItem(1, 100));
        Assertions.assertEquals("404 NOT_FOUND \"Некорректный id вещи. Попробуйте еще раз.\"", exeption.getMessage());
    }


    @Test
    void getItemIncorrectTest() {
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                itemService.getItem(1, 100));
        Assertions.assertEquals("404 NOT_FOUND \"Некорректный id вещи. Попробуйте еще раз.\"", exeption.getMessage());
    }

    @Test
    void checkIdTest() {
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                itemService.getItems(-1, 0, 10));
        Assertions.assertEquals("404 NOT_FOUND \"Некорректный id. Попробуйте еще раз.\"", exeption.getMessage());
    }

    @Test
    void checkPageTest() {
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                itemService.getItems(1, -1, 10));
        Assertions.assertEquals("400 BAD_REQUEST \"Некорректный индекс пагинации. Попробуйте еще раз.\"", exeption.getMessage());
    }

}
