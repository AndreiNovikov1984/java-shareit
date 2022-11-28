package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
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
    public Collection<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userID) {
        return itemService.getItems(userID);
    }

    @GetMapping("/{id}")                                // метод получения вещи по Id
    public ItemDto getItem(@PathVariable long id) {
        return itemService.getItem(id);
    }

    @GetMapping("/search")                              // метод поиска вещи
    public Collection<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping                                        // метод создания новой вещи
    public ItemDto postItem(@RequestHeader("X-Sharer-User-Id") long userID, @RequestBody ItemDto itemDto) {
        return itemService.postItem(userID, itemDto);
    }

    @PatchMapping("/{id}")                              // метод обновления вещи
    public ItemDto patchItem(@RequestHeader("X-Sharer-User-Id") long userID, @PathVariable long id, @RequestBody ItemDto itemDto) {
        return itemService.patchItem(userID, id, itemDto);
    }
}
