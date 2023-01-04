package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @GetMapping                                                 // метод получения списка вещей по ID пользователя
    public ResponseEntity<Collection<ItemRequestAnswerDto>> getRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        Collection<ItemRequestAnswerDto> itemRequestDtoUpd = itemRequestService.getRequests(userId);
        return new ResponseEntity<>(itemRequestDtoUpd, HttpStatus.OK);
    }

    @GetMapping("/{requestId}")
    // метод получения списка вещей по ID пользователя
    public ResponseEntity<ItemRequestAnswerDto> getRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                           @PathVariable long requestId) {
        ItemRequestAnswerDto itemRequestDtoUpd = itemRequestService.getRequest(userId, requestId);
        return new ResponseEntity<>(itemRequestDtoUpd, HttpStatus.OK);
    }

    @GetMapping("/all")                                   // метод получения списка вещей по ID пользователя
    public ResponseEntity<Collection<ItemRequestAnswerDto>> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                                           @RequestParam(defaultValue = "0") int from,
                                                                           @RequestParam(defaultValue = "20") int size) {
        Collection<ItemRequestAnswerDto> itemRequestDtoUpd = itemRequestService.getAllRequests(userId, from, size);
        return new ResponseEntity<>(itemRequestDtoUpd, HttpStatus.OK);
    }

    @PostMapping                                        // метод создания нового запроса на вещь
    public ResponseEntity<ItemRequestDto> postRequest(@RequestHeader("X-Sharer-User-Id") long userID,
                                                      @RequestBody ItemRequestDto itemRequestDto) {
        ItemRequestDto itemRequestDtoUpd = itemRequestService.postRequest(userID, itemRequestDto);
        return new ResponseEntity<>(itemRequestDtoUpd, HttpStatus.OK);
    }

}
