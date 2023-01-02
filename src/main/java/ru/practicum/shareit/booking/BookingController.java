package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
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
public class BookingController {
    private final BookingService bookingService;

    @GetMapping                                              // метод данных о всех бронированиях пользователя
    public ResponseEntity<List<BookingDto>> getBookings(@RequestHeader("X-Sharer-User-Id") long userID,
                                                        @RequestParam(defaultValue = "ALL") TypeStatusDto state,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "20") int size) {
        return new ResponseEntity<>(bookingService.getBookings(userID, state, from, size), HttpStatus.OK);
    }

    @GetMapping("/owner")                 // метод получения данных о бронированиях всех вещей пользователя
    public ResponseEntity<List<BookingDto>> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userID,
                                                             @RequestParam(defaultValue = "ALL") TypeStatusDto state,
                                                             @RequestParam(defaultValue = "0") int from,
                                                             @RequestParam(defaultValue = "20") int size) {
        return new ResponseEntity<>(bookingService.getOwnerBookings(userID, state, from, size), HttpStatus.OK);
    }

    @GetMapping({"/{id}"})                                      // метод получения данных о бронировании
    public ResponseEntity<BookingDto> getBooking(@RequestHeader("X-Sharer-User-Id") long userID, @PathVariable long id) {
        return new ResponseEntity<>(bookingService.getBooking(userID, id), HttpStatus.OK);
    }

    @PostMapping                                        // метод создания нового запроса на бронирование
    public ResponseEntity<BookingDto> postBooking(@RequestHeader("X-Sharer-User-Id") long userID, @RequestBody BookingDto bookingDto) {
        return new ResponseEntity<>(bookingService.postBooking(userID, bookingDto), HttpStatus.OK);
    }

    @PatchMapping({"/{id}"})        // метод подтверждения/отклонения запроса на бронирование
    public ResponseEntity<BookingDto> patchBooking(@RequestHeader("X-Sharer-User-Id") long userID, @RequestParam String approved, @PathVariable long id) {
        return new ResponseEntity<>(bookingService.patchbooking(userID, approved, id), HttpStatus.OK);
    }

}
