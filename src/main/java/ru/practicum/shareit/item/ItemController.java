package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping                                                 // метод получения списка вещей по ID пользователя
    public ResponseEntity<Collection<ItemDto>> getItems(@RequestHeader("X-Sharer-User-Id") long userID) {
        Collection<ItemDto> items = itemService.getItems(userID);
        return items != null && !items.isEmpty()
                ? new ResponseEntity<>(items, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")                                // метод получения вещи по Id
    public ResponseEntity<ItemDto> getItem(@PathVariable long id) {
        return new ResponseEntity<>(itemService.getItem(id), HttpStatus.OK);
    }

    @GetMapping("/search")                              // метод поиска вещи
    public ResponseEntity<Collection<ItemDto>> search(@RequestParam String text) {
        return new ResponseEntity<>(itemService.search(text), HttpStatus.OK);
    }

    @PostMapping                                        // метод создания новой вещи
    public ResponseEntity<ItemDto> postItem(@RequestHeader("X-Sharer-User-Id") long userID, @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.postItem(userID, itemDto), HttpStatus.OK);
    }

    @PatchMapping("/{id}")                              // метод обновления вещи
    public ResponseEntity<ItemDto> patchItem(@RequestHeader("X-Sharer-User-Id") long userID, @PathVariable long id, @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemService.patchItem(userID, id, itemDto), HttpStatus.OK);
    }
}
