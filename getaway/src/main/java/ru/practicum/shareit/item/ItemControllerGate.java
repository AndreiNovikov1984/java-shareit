package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoGate;
import ru.practicum.shareit.item.dto.ItemDtoGate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemControllerGate {
    private final ItemClient itemClient;

    @GetMapping                                                 // метод получения списка вещей по ID пользователя
    public ResponseEntity<Object> getItems(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                           @Positive @RequestParam(defaultValue = "20") int size) {
        log.info("Get items with userId={}, from={}, size={}", userId, from, size);
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{itemId}")                                // метод получения вещи по Id
    public ResponseEntity<Object> getItem(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                          @Positive @PathVariable long itemId) {
        log.info("Get item with userId={}, itemId={}", userId, itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping("/search")                              // метод поиска вещи
    public ResponseEntity<Object> search(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam String text,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Positive @RequestParam(defaultValue = "20") int size) {
        log.info("Search items with userId={}, text={}, from={}, size={}", userId, text, from, size);
        return itemClient.search(userId, text, from, size);
    }

    @PostMapping                                        // метод создания новой вещи
    public ResponseEntity<Object> postItem(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody ItemDtoGate itemDto) {
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemClient.postItem(userId, itemDto);
    }


    @PostMapping("/{itemId}/comment")                                        // метод создания новой вещи
    public ResponseEntity<Object> postComment(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                              @Positive @PathVariable long itemId,
                                              @RequestBody CommentDtoGate commentDto) {
        log.info("Creating comment {}, userId={}", commentDto, userId);
        return itemClient.postComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")                              // метод обновления вещи
    public ResponseEntity<Object> patchItem(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                            @Positive @PathVariable long itemId,
                                            @RequestBody ItemDtoGate itemDto) {
        log.info("Patching itemId={}, userId={}", itemId, userId);
        return itemClient.patchItem(userId, itemId, itemDto);
    }
}