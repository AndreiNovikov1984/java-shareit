package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoGate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestControllerGate {
    private final ItemRequestClient itemRequestClient;

    @GetMapping                                                 // метод получения запросов вещей по ID пользователя
    public ResponseEntity<Object> getRequests(@Positive @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get itemRequests with userId={}", userId);
        return itemRequestClient.getRequests(userId);
    }

    @GetMapping("/{requestId}")                             // метод получения запроса вещи по ID пользователя
    public ResponseEntity<Object> getRequest(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Positive @PathVariable long requestId) {
        log.info("Get itemRequest with requestId={}, userId={}", requestId, userId);
        return itemRequestClient.getRequest(userId, requestId);
    }

    @GetMapping("/all")                                   // метод получения всех запросов вещей по ID пользователя
    public ResponseEntity<Object> getAllRequests(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                 @Positive @RequestParam(defaultValue = "20") int size) {
        log.info("Get ALL itemRequests with userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getAllRequests(userId, from, size);
    }

    @PostMapping                                        // метод создания нового запроса на вещь
    public ResponseEntity<Object> postRequest(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestBody ItemRequestDtoGate itemRequestDto) {
        log.info("Creating itemRequest {}, userId={}", itemRequestDto, userId);
        return itemRequestClient.postRequest(userId, itemRequestDto);
    }
}
