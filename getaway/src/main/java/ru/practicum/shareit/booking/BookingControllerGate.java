package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoGate;
import ru.practicum.shareit.booking.dto.TypeStatusDtoGate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingControllerGate {
    private final BookingClient bookingClient;

    @GetMapping                                              // метод данных о всех бронированиях пользователя
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "ALL") TypeStatusDtoGate state,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "20") Integer size) {
        log.info("Get bookings with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")                 // метод получения данных о бронированиях всех вещей пользователя
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") TypeStatusDtoGate state,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "20") Integer size) {
        log.info("Get bookings all items by owner with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getOwnerBookings(userId, state, from, size);
    }

    @GetMapping("/{bookingId}")                                      // метод получения данных о бронировании
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }


    @PostMapping                                        // метод создания нового запроса на бронирование
    public ResponseEntity<Object> postBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestBody BookingDtoGate bookingDto) {
        log.info("Creating booking {}, userId={}", bookingDto, userId);
        return bookingClient.postBooking(userId, bookingDto);
    }

    @PatchMapping({"/{bookingId}"})        // метод подтверждения/отклонения запроса на бронирование
    public ResponseEntity<Object> patchBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam String approved,
                                               @PathVariable Long bookingId) {
        log.info("Response on booking with bookingId {} для userId={}, approved = {}", bookingId, userId, approved);
        return bookingClient.patchBooking(userId, approved, bookingId);
    }
}
