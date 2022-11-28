package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> itemMap = new HashMap<>();
    private static long identificator = 0;

    public Collection<Item> getAllItems(Long userID) {      // метод получения списка вещей по ID пользователя
        log.info("Информация о вещах пользователя ID = {} передана", userID);
        return itemMap.values().stream()
                .filter(p -> p.itemOwnerId == userID)
                .collect(Collectors.toList());
    }

    public Item getItembyId(Long itemId) {                  // метод получения вещи по ID
        log.info("Информация о вещи ID = {} передана", itemId);
        return itemMap.get(itemId);
    }

    public Collection<Item> searchItem(String text) {           // метод поиска вещи
        if (text.equals("")) {
            return new ArrayList<>();
        } else {
            return itemMap.values().stream()
                    .filter(p -> (p.getName().toLowerCase().contains(text) ||
                            p.getDescription().toLowerCase().contains(text)) &&
                            p.getAvailable())
                    .collect(Collectors.toList());
        }
    }

    public Item addItem(Item item) {                // метод добавления вещи
        if (!itemMap.containsKey(item.getId())) {
            identificator++;
            item.setId(identificator);
        }
        itemMap.put(item.getId(), item);
        log.info("Вещь ID = {} добавлена", item.getId());
        return item;
    }

    public Item updateItem(Item item) {                 // метод обновления вещи
        if (item.getItemOwnerId() != itemMap.get(item.getId()).getItemOwnerId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не является владельцем данной вещи.");
        }
        if (item.getName() == null) {
            item.setName(itemMap.get(item.getId()).getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(itemMap.get(item.getId()).getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(itemMap.get(item.getId()).getAvailable());
        }
        itemMap.put(item.getId(), item);
        log.info("Вещь ID = {} обновлена", item.getId());
        return item;
    }
}
