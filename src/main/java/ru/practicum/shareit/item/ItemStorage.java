package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Collection<Item> getAllItems(Long userID);

    Item getItembyId(Long itemId);

    Collection<Item> searchItem(String text);

    Item addItem(Item item);

    Item updateItem(Item item);

}
