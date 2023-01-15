package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.TypeStatusDto;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @GetMapping                                              // метод данных о всех бронированиях пользователя
    public ResponseEntity<List<BookingDto>> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @RequestParam(defaultValue = "ALL") TypeStatusDto state,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "20") int size) {
        log.info("Получение бронирования со статусом {}, для userId={}, from={}, size={}", state, userId, from, size);
        return new ResponseEntity<>(bookingService.getBookings(userId, state, from, size), HttpStatus.OK);
    }

    @GetMapping("/owner")                 // метод получения данных о бронированиях всех вещей пользователя
    public ResponseEntity<List<BookingDto>> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                             @RequestParam(defaultValue = "ALL") TypeStatusDto state,
                                                             @RequestParam(defaultValue = "0") int from,
                                                             @RequestParam(defaultValue = "20") int size) {
        log.info("Получение бронирований со статусом {}, для userId={}, from={}, size={}", state, userId, from, size);
        return new ResponseEntity<>(bookingService.getOwnerBookings(userId, state, from, size), HttpStatus.OK);
    }

    @GetMapping({"/{bookingId}"})                                      // метод получения данных о бронировании
    public ResponseEntity<BookingDto> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable long bookingId) {
        log.info("Получение бронирования bookingId={} для userId={}", bookingId, userId);
        return new ResponseEntity<>(bookingService.getBooking(userId, bookingId), HttpStatus.OK);
    }

    @PostMapping                                        // метод создания нового запроса на бронирование
    public ResponseEntity<BookingDto> postBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestBody BookingDto bookingDto) {
        log.info("Создание бронирования booking {} для userId={}", bookingDto, userId);
                return new ResponseEntity<>(bookingService.postBooking(userId, bookingDto), HttpStatus.OK);
    }

    @PatchMapping({"/{bookingId}"})        // метод подтверждения/отклонения запроса на бронирование
    public ResponseEntity<BookingDto> patchBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestParam String approved,
                                                   @PathVariable long bookingId) {
        log.info("Ответ на запрос бронирования bookingId {} для userId={}", bookingId, userId);
        return new ResponseEntity<>(bookingService.patchbooking(userId, approved, bookingId), HttpStatus.OK);
    }
}
