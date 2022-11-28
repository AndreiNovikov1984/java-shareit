package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {
    public ItemDto convertItemToDto(Item item) {            // метод преобразования Item в ItemDto
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest())
                .itemFeedback(item.getItemFeedback())
                .build();
    }

    public Item convertDtoToItem(long userID, ItemDto itemDto) {       // метод преобразования ItemDto в Item
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .itemOwnerId(userID)
                .request(itemDto.getRequest())
                .itemFeedback(itemDto.getItemFeedback())
                .build();
    }
}
