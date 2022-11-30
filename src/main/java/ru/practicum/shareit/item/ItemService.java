package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.support.Validation;
import ru.practicum.shareit.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;
    private final UserStorage userStorage;

    public Collection<ItemDto> getItems(long userID) {        // метод получения списка вещей по ID пользователя
        checkId(userID);
        return itemStorage.getAllItems(userID).stream()
                .map(itemMapper::convertItemToDto)
                .collect(Collectors.toList());
    }

    public ItemDto getItem(long itemId) {                               // метод получения вещи по ID
        checkId(itemId);
        return itemMapper.convertItemToDto(itemStorage.getItembyId(itemId));
    }

    public Collection<ItemDto> search(String text) {                            // метод поиска вещи
        if (text != null) {
            text = text.toLowerCase();
        }
        return itemStorage.searchItem(text).stream()
                .map(itemMapper::convertItemToDto)
                .collect(Collectors.toList());
    }

    public ItemDto postItem(long userID, ItemDto itemDto) {         // метод добавления вещи
        checkId(userID);
        userStorage.getUserById(userID);
        Item item = itemMapper.convertDtoToItem(userID, itemDto);
        Validation.validationItem(item);
        return itemMapper.convertItemToDto(itemStorage.addItem(item));
    }

    public ItemDto patchItem(long userID, long itemId, ItemDto itemDto) { // метод обновления вещи
        checkId(userID);
        userStorage.getUserById(userID);
        checkId(itemId);
        itemDto.setId(itemId);
        Item item = itemMapper.convertDtoToItem(userID, itemDto);
        return itemMapper.convertItemToDto(itemStorage.updateItem(item));
    }

    private void checkId(long id) {
        if ((id < 0) || (id == 0)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id. Попробуйте еще раз.");
        }
    }
}
