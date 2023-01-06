package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class ItemController {
    private final ItemService itemService;
    private final CommentMapper commentMapper;

    @GetMapping                                                 // метод получения списка вещей по ID пользователя
    public ResponseEntity<Collection<ItemDto>> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "20") int size) {
        return new ResponseEntity<>(itemService.getItems(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")                                // метод получения вещи по Id
    public ResponseEntity<ItemDto> getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        return new ResponseEntity<>(itemService.getItem(userId, id), HttpStatus.OK);
    }

    @GetMapping("/search")                              // метод поиска вещи
    public ResponseEntity<Collection<ItemDto>> search(@RequestParam String text,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "20") int size) {
        return new ResponseEntity<>(itemService.search(text, from, size), HttpStatus.OK);
    }

    @PostMapping                                        // метод создания новой вещи
    public ResponseEntity<ItemDto> postItem(@RequestHeader("X-Sharer-User-Id") long userID,
                                            @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.postItem(userID, itemDto), HttpStatus.OK);
    }

    @PostMapping("/{id}/comment")                                        // метод создания новой вещи
    public ResponseEntity<CommentDto> postComment(@RequestHeader("X-Sharer-User-Id") long userID,
                                                  @PathVariable long id,
                                                  @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(itemService.postComment(userID, id, commentMapper.convertDtoToComment(commentDto)), HttpStatus.OK);
    }

    @PatchMapping("/{id}")                              // метод обновления вещи
    public ResponseEntity<ItemDto> patchItem(@RequestHeader("X-Sharer-User-Id") long userID,
                                             @PathVariable long id,
                                             @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.patchItem(userID, id, itemDto), HttpStatus.OK);
    }
}
