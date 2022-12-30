package ru.practicum.shareit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"file:src/test/resources/schema.sql",
        "file:src/test/resources/test.sql"})
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserStorage userStorage;


    @Test
    void getAllUsersTest() throws Exception {
        mockMvc.perform(
                        get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    @Test
    void getUserByIdTest() throws Exception {
        mockMvc.perform(
                        get("/users/" + 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan1"))
                .andExpect(jsonPath("$.email").value("ivan1@mail.ru"));
    }

    @Test
    void postUserTest() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("Ivan")
                .email("van@ya.com")
                .build();

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.email").value("van@ya.com"))
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    @Test
    void postUserWithoutNameTest() throws Exception {
        UserDto userDto = UserDto.builder()
                .email("van@ya.com")
                .build();

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"Имя пользователя не может быть пустым или содержать пробелы.\"",
                        result.getResponse().getContentAsString()));
    }

    @Test
    void postUserWithoutEmailTest() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("Ivan")
                .build();

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"E-mail не корректный. Попробуйте еще раз.\"",
                        result.getResponse().getContentAsString()));
    }

    @Test
    void postUserWithIncorrectEmailTest() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("Ivan")
                .email("van-ya.com")
                .build();

        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"E-mail не корректный. Попробуйте еще раз.\"",
                        result.getResponse().getContentAsString()));
    }

    @Test
    void patchUserTest() throws Exception {
        User user = User.builder()
                .name("Ivan")
                .email("van@ya.com")
                .build();
        long id = userStorage.save(user).getId();

        user.setName("Ivan2");
        user.setEmail("van@ya2.com");


        mockMvc.perform(
                        patch("/users/" + id)
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ivan2"))
                .andExpect(jsonPath("$.email").value("van@ya2.com"));
    }

    @Test
    void deleteUserTest() throws Exception {
        User user = User.builder()
                .name("Ivan")
                .email("van@ya.com")
                .build();
        long id = userStorage.save(user).getId();

        mockMvc.perform(
                        delete("/users/" + id))
                .andExpect(status().isOk());
    }
}
