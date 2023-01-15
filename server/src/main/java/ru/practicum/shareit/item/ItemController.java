package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final CommentMapper commentMapper;

    @GetMapping                                                 // метод получения списка вещей по ID пользователя
    public ResponseEntity<Collection<ItemDto>> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "20") int size) {
        log.info("Получение вещей для userId={}, from={}, size={}", userId, from, size);
        return new ResponseEntity<>(itemService.getItems(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")                                // метод получения вещи по Id
    public ResponseEntity<ItemDto> getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Получение вещи itemId={} для userId={}", itemId, userId);
        return new ResponseEntity<>(itemService.getItem(userId, itemId), HttpStatus.OK);
    }

    @GetMapping("/search")                              // метод поиска вещи
    public ResponseEntity<Collection<ItemDto>> search(@RequestParam String text,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "20") int size) {
        log.info("Поиск вещи по text={}, from={}, size={}", text, from, size);
        return new ResponseEntity<>(itemService.search(text, from, size), HttpStatus.OK);
    }

    @PostMapping                                        // метод создания новой вещи
    public ResponseEntity<ItemDto> postItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody ItemDto itemDto) {
        log.info("Добавление вещи {} для userId={}", itemDto, userId);
        return new ResponseEntity<>(itemService.postItem(userId, itemDto), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")                                        // метод создания новой вещи
    public ResponseEntity<CommentDto> postComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @PathVariable long itemId,
                                                  @RequestBody CommentDto commentDto) {
        log.info("Добавление комментария {} для вещи itemId={}, userId={}", commentDto, itemId, userId);
        return new ResponseEntity<>(itemService.postComment(userId, itemId, commentMapper.convertDtoToComment(commentDto)), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")                              // метод обновления вещи
    public ResponseEntity<ItemDto> patchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("Обновление вещи itemId={}, userId={}", itemId, userId);
        return new ResponseEntity<>(itemService.patchItem(userId, itemId, itemDto), HttpStatus.OK);
    }
}
