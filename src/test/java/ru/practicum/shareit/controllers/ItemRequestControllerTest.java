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
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"file:src/test/resources/schema.sql",
        "file:src/test/resources/test.sql"})
public class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllItemRequestsTest() throws Exception {
        mockMvc.perform(
                        get("/requests")
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void getAllItemRequestsWithIncorrectUserTest() throws Exception {
        mockMvc.perform(
                        get("/requests")
                                .header("X-Sharer-User-Id", 100))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Некорректный id пользователя. Попробуйте еще раз.\"",
                        result.getResponse().getContentAsString()));
    }

    @Test
    void getRequestTest() throws Exception {
        mockMvc.perform(
                        get("/requests/1")
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Thing1 needed"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getRequestIncorrectIdTest() throws Exception {
        mockMvc.perform(
                        get("/requests/100")
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Некорректный id запроса вещи. Попробуйте еще раз.\"",
                        result.getResponse().getContentAsString()));
    }

    @Test
    void getAllRequestsTest() throws Exception {
        mockMvc.perform(
                        get("/requests/all")
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    void getAllRequestsEmptyTest() throws Exception {
        mockMvc.perform(
                        get("/requests/all?from=2&size=1")
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    void postRequestTest() throws Exception {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("Thing needed")
                .created(LocalDateTime.now())
                .build();

        mockMvc.perform(
                        post("/requests")
                                .header("X-Sharer-User-Id", 1)
                                .content(objectMapper.writeValueAsString(itemRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.description").value("Thing needed"));
    }

    @Test
    void postRequestIncorrectTest() throws Exception {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("Thing needed")
                .created(LocalDateTime.now())
                .build();

        mockMvc.perform(
                        post("/requests")
                                .header("X-Sharer-User-Id", 100)
                                .content(objectMapper.writeValueAsString(itemRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
