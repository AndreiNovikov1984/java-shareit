package ru.practicum.shareit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserStorage userStorage;

    @Test
    void getItemsTest() throws Exception {
        Item item = Item.builder()
                .id(1)
                .name("Молоток")
                .description("Хороший молоток")
                .available(true)
                .itemOwnerId(1)
                .build();
        long id = itemStorage.addItem(item).getId();

        mockMvc.perform(
                        get("/items")
                                .header("X-Sharer-User-Id", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void getItemByIdTest() throws Exception {
        Item item = Item.builder()
                .id(1)
                .name("Молоток")
                .description("Хороший молоток")
                .available(true)
                .itemOwnerId(1)
                .build();
        long id = itemStorage.addItem(item).getId();

        mockMvc.perform(
                        get("/items/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Молоток"))
                .andExpect(jsonPath("$.description").value("Хороший молоток"));
    }

    @Test
    void searchItemTest() throws Exception {
        Item item = Item.builder()
                .id(1)
                .name("Молоток")
                .description("Хороший молоток")
                .available(true)
                .itemOwnerId(1)
                .build();
        long id = itemStorage.addItem(item).getId();

        mockMvc.perform(
                        get("/items/search?text=Молоток"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void searchItemEmptyTest() throws Exception {
        Item item = Item.builder()
                .id(1)
                .name("Молоток")
                .description("Хороший молоток")
                .available(true)
                .itemOwnerId(1)
                .build();
        long id = itemStorage.addItem(item).getId();

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
                .build();

        User user = User.builder()
                .name("Ivan")
                .email("van@ya.com")
                .build();
        long userId = userStorage.addUser(user).getId();

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
                .build();

        mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", 1)
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
        long userId = userStorage.addUser(user).getId();

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
        long userId = userStorage.addUser(user).getId();

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
        long userId = userStorage.addUser(user).getId();

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
        User user = User.builder()
                .name("Ivan")
                .email("van@ya.com")
                .build();
        long userId = userStorage.addUser(user).getId();

        Item item = Item.builder()
                .name("Молоток")
                .description("Хороший молоток")
                .available(true)
                .itemOwnerId(userId)
                .build();
        long id = itemStorage.addItem(item).getId();

        item.setName("Молот");
        item.setDescription("Хороший молот");

        mockMvc.perform(
                        patch("/items/" + id)
                                .header("X-Sharer-User-Id", userId)
                                .content(objectMapper.writeValueAsString(item))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Молот"))
                .andExpect(jsonPath("$.description").value("Хороший молот"));
    }

    @Test
    void patchItemsWithoutUserTest() throws Exception {
        Item item = Item.builder()
                .name("Молоток")
                .description("Хороший молоток")
                .available(true)
                .itemOwnerId(1)
                .build();
        long id = itemStorage.addItem(item).getId();

        mockMvc.perform(
                        patch("/items/" + id)
                                .header("X-Sharer-User-Id", 1)
                                .content(objectMapper.writeValueAsString(item))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchItemsWithIncorrectUserIdTest() throws Exception {
        User user1 = User.builder()
                .name("Ivan")
                .email("van@ya.com")
                .build();
        long userId1 = userStorage.addUser(user1).getId();
        User user2 = User.builder()
                .name("Ivan2")
                .email("van@ya2.com")
                .build();
        long userId2 = userStorage.addUser(user2).getId();

        Item item = Item.builder()
                .name("Молоток")
                .description("Хороший молоток")
                .available(true)
                .itemOwnerId(userId1)
                .build();
        long id = itemStorage.addItem(item).getId();

        mockMvc.perform(
                        patch("/items/" + id)
                                .header("X-Sharer-User-Id", userId2)
                                .content(objectMapper.writeValueAsString(item))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Пользователь не является владельцем данной вещи.\"",
                        result.getResponse().getContentAsString()));
    }

}
