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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;

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
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userStorage;

    @Test
    void getItemsTest() throws Exception {

        mockMvc.perform(
                        get("/items")
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void getItemByIdTest() throws Exception {

        mockMvc.perform(
                        get("/items/" + 1)
                                .header("X-Sharer-User-Id", 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Thing1"))
                .andExpect(jsonPath("$.description").value("Thing1 description"));
    }

    @Test
    void searchItemTest() throws Exception {
        mockMvc.perform(
                        get("/items/search?text=Thing1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void searchItemEmptyTest() throws Exception {
        mockMvc.perform(
                        get("/items/search?text="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    void postItemsTest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Хороший молоток")
                .available(true)
                .comments(new ArrayList<>())
                .build();

        User user = User.builder()
                .name("Ivan")
                .email("van@ya.com")
                .build();
        long userId = userStorage.save(user).getId();


        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", userId)
                                .content(objectMapper.writeValueAsString(itemDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Молоток"))
                .andExpect(jsonPath("$.description").value("Хороший молоток"));

    }

    @Test
    void postItemsIncorrectUserTest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Хороший молоток")
                .available(true)
                .comments(new ArrayList<>())
                .build();

        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", 4)
                                .content(objectMapper.writeValueAsString(itemDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void postItemsWithoutNameTest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .description("Хороший молоток")
                .available(true)
                .build();

        User user = User.builder()
                .name("Ivan")
                .email("van@ya.com")
                .build();
        long userId = userStorage.save(user).getId();

        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", userId)
                                .content(objectMapper.writeValueAsString(itemDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"Имя вещи не может быть пустым.\"",
                        result.getResponse().getContentAsString()));
    }

    @Test
    void postItemsWithoutDescriptionTest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .available(true)
                .build();

        User user = User.builder()
                .name("Ivan")
                .email("van@ya.com")
                .build();
        long userId = userStorage.save(user).getId();

        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", userId)
                                .content(objectMapper.writeValueAsString(itemDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"Описание вещи не может быть пустым.\"",
                        result.getResponse().getContentAsString()));
    }

    @Test
    void postItemsWithoutAvailableTest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .name("Молоток")
                .description("Хороший молоток")
                .build();

        User user = User.builder()
                .name("Ivan")
                .email("van@ya.com")
                .build();
        long userId = userStorage.save(user).getId();

        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", userId)
                                .content(objectMapper.writeValueAsString(itemDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"Поле доступности должно быть заполнено.\"",
                        result.getResponse().getContentAsString()));
    }

    @Test
    void patchItemsTest() throws Exception {
        Item item = Item.builder()
                .id(1)
                .name("Thing1Upd")
                .description("Thing1 description_Upd")
                .available(true)
                .owner(userStorage.findById(1L).get())
                .build();

        mockMvc.perform(
                        patch("/items/" + 1)
                                .header("X-Sharer-User-Id", 1)
                                .content(objectMapper.writeValueAsString(item))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Thing1Upd"))
                .andExpect(jsonPath("$.description").value("Thing1 description_Upd"));
    }

    @Test
    void patchItemsWithoutUserTest() throws Exception {
        Item item = Item.builder()
                .id(1)
                .name("Thing1Upd")
                .description("Thing1 description_Upd")
                .available(true)
                .owner(userStorage.findById(1L).get())
                .build();

        mockMvc.perform(
                        patch("/items/" + 1)
                                .header("X-Sharer-User-Id", 4)
                                .content(objectMapper.writeValueAsString(item))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchItemsWithIncorrectUserIdTest() throws Exception {
        Item item = Item.builder()
                .id(1)
                .name("Thing1Upd")
                .description("Thing1 description_Upd")
                .available(true)
                .owner(userStorage.findById(1L).get())
                .build();

        mockMvc.perform(
                        patch("/items/" + 1)
                                .header("X-Sharer-User-Id", 2)
                                .content(objectMapper.writeValueAsString(item))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Пользователь не является владельцем данной вещи.\"",
                        result.getResponse().getContentAsString()));
    }
}
