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
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"file:src/test/resources/schema.sql",
        "file:src/test/resources/test.sql"})
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getBookingsTest() throws Exception {

        mockMvc.perform(
                        get("/bookings")
                                .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    void getBookingsStateCurrentTest() throws Exception {

        mockMvc.perform(
                        get("/bookings?state=CURRENT")
                                .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    void getBookingsStatePastTest() throws Exception {

        mockMvc.perform(
                        get("/bookings?state=PAST")
                                .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    void getBookingsStateFutureTest() throws Exception {

        mockMvc.perform(
                        get("/bookings?state=FUTURE")
                                .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    void getBookingsStateWaitingTest() throws Exception {

        mockMvc.perform(
                        get("/bookings?state=WAITING")
                                .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void getBookingsStateRejectedTest() throws Exception {

        mockMvc.perform(
                        get("/bookings?state=REJECTED")
                                .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    void getOwnerBookingsStateCurrentTest() throws Exception {

        mockMvc.perform(
                        get("/bookings/owner?state=CURRENT")
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    void getOwnerBookingsStatePastTest() throws Exception {

        mockMvc.perform(
                        get("/bookings/owner?state=PAST")
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void getOwnerBookingsStateFutureTest() throws Exception {

        mockMvc.perform(
                        get("/bookings/owner?state=FUTURE")
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    void getOwnerBookingsStateWaitingTest() throws Exception {

        mockMvc.perform(
                        get("/bookings/owner?state=WAITING")
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    void getOwnerBookingsStateRejectiongTest() throws Exception {

        mockMvc.perform(
                        get("/bookings/owner?state=REJECTED")
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    void getOwnerBookingsTest() throws Exception {

        mockMvc.perform(
                        get("/bookings/owner")
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void getBookingTest() throws Exception {

        mockMvc.perform(
                        get("/bookings/1")
                                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booker.id").value(2))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void postBookingTest() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1)
                .build();

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 2)
                                .content(objectMapper.writeValueAsString(bookingDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.id").value("1"))
                .andExpect(jsonPath("$.booker.id").value("2"))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void postBookingWithoutItemTest() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(5)
                .build();

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 2)
                                .content(objectMapper.writeValueAsString(bookingDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void postBookingWithoutUserTest() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1)
                .build();

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 6)
                                .content(objectMapper.writeValueAsString(bookingDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void postBookingWithIncorrectStartDataTest() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1)
                .build();

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 6)
                                .content(objectMapper.writeValueAsString(bookingDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void postBookingWithIncorrectEndDataTest() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().minusDays(2))
                .itemId(1)
                .build();

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 6)
                                .content(objectMapper.writeValueAsString(bookingDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void postBookingWithEndBeforeStartDataTest() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(1))
                .itemId(1)
                .build();

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 6)
                                .content(objectMapper.writeValueAsString(bookingDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchBookingTest() throws Exception {
        mockMvc.perform(
                        patch("/bookings/3?approved=true")
                                .header("X-Sharer-User-Id", 3)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.id").value("3"))
                .andExpect(jsonPath("$.booker.id").value("2"))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void patchBookingNotOwnerTest() throws Exception {
        mockMvc.perform(
                        patch("/bookings/3?approved=true")
                                .header("X-Sharer-User-Id", 2)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
