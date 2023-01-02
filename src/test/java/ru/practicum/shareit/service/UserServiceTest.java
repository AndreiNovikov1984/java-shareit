package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.UserService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"file:src/test/resources/schema.sql",
        "file:src/test/resources/test.sql"})
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void checkIdTest() {
        ResponseStatusException exeption = Assertions.assertThrows(ResponseStatusException.class, () ->
                userService.getUserWithId(-1));
        Assertions.assertEquals("404 NOT_FOUND \"Некорректный id. Попробуйте еще раз.\"", exeption.getMessage());
    }
}
