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
    public ResponseEntity<Collection<ItemDto>> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        Collection<ItemDto> items = itemService.getItems(userId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{id}")                                // метод получения вещи по Id
    public ResponseEntity<ItemDto> getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        ItemDto itemResponce = itemService.getItem(userId, id);
        return new ResponseEntity<>(itemResponce, HttpStatus.OK);
    }

    @GetMapping("/search")                              // метод поиска вещи
    public ResponseEntity<Collection<ItemDto>> search(@RequestParam String text) {
        return new ResponseEntity<>(itemService.search(text), HttpStatus.OK);
    }

    @PostMapping                                        // метод создания новой вещи
    public ResponseEntity<ItemDto> postItem(@RequestHeader("X-Sharer-User-Id") long userID, @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.postItem(userID, itemDto), HttpStatus.OK);
    }

    @PostMapping("/{id}/comment")                                        // метод создания новой вещи
    public ResponseEntity<CommentDto> postComment(@RequestHeader("X-Sharer-User-Id") long userID, @PathVariable long id, @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(itemService.postComment(userID, id, commentMapper.convertDtoToComment(commentDto)), HttpStatus.OK);
    }

    @PatchMapping("/{id}")                              // метод обновления вещи
    public ResponseEntity<ItemDto> patchItem(@RequestHeader("X-Sharer-User-Id") long userID, @PathVariable long id, @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.patchItem(userID, id, itemDto), HttpStatus.OK);
    }
}
