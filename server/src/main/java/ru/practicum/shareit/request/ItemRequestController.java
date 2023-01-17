package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @GetMapping                                           // метод получения запросов вещей по ID пользователя
    public ResponseEntity<Collection<ItemRequestAnswerDto>> getRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение запросов вещей для userId={}", userId);
        return new ResponseEntity<>(itemRequestService.getRequests(userId), HttpStatus.OK);
    }

    @GetMapping("/{requestId}")                             // метод получения запроса вещи по ID пользователя
    public ResponseEntity<ItemRequestAnswerDto> getRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @PathVariable Long requestId) {
        log.info("Получение запроса вещи requestId={} для userId={}", requestId, userId);
        return new ResponseEntity<>(itemRequestService.getRequest(userId, requestId), HttpStatus.OK);
    }

    @GetMapping("/all")                                   // метод получения всех запросов вещей по ID пользователя
    public ResponseEntity<Collection<ItemRequestAnswerDto>> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                           @RequestParam(defaultValue = "0") Integer from,
                                                                           @RequestParam(defaultValue = "20") Integer size) {
        log.info("Получение всех запросов вещей для userId={}, from={}, size={}", userId, from, size);
        return new ResponseEntity<>(itemRequestService.getAllRequests(userId, from, size), HttpStatus.OK);
    }

    @PostMapping                                        // метод создания нового запроса на вещь
    public ResponseEntity<ItemRequestDto> postRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Создание запроса вещи {} для userId={}", itemRequestDto, userId);
        return new ResponseEntity<>(itemRequestService.postRequest(userId, itemRequestDto), HttpStatus.OK);
    }
}
