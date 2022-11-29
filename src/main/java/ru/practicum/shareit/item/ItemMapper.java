package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto convertItemToDto(Item item);             // метод преобразования Item в ItemDto

    @Mapping(source = "userID", target = "itemOwnerId")
    Item convertDtoToItem(long userID, ItemDto itemDto);        // метод преобразования ItemDto в Item
}
