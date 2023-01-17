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
@Sql(scripts = {"file:../server/src/test/resources/schema.sql",
        "file:../server/src/test/resources/test.sql"})
public class ItemServiceTest {
    @Autowired
    private ItemService itemService;
    private ResponseStatusException exeption;

    @Test
    void getItemTest() {
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                itemService.getItem(1L, 100L));
        Assertions.assertEquals("404 NOT_FOUND \"Некорректный id вещи. Попробуйте еще раз.\"", exeption.getMessage());
    }


    @Test
    void getItemIncorrectTest() {
        exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                itemService.getItem(1L, 100L));
        Assertions.assertEquals("404 NOT_FOUND \"Некорректный id вещи. Попробуйте еще раз.\"", exeption.getMessage());
    }
}
