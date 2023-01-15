package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;


@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ItemMapper {
    ItemDto convertItemToDto(Item item);             // метод преобразования Item в ItemDto

    @Mapping(target = "owner", ignore = true)
    Item convertDtoToItem(ItemDto itemDto);        // метод преобразования ItemDto в Item

    @Mapping(source = "owner.id", target = "ownerId")
    ItemAnswerDto convertItemToAnswerDto(Item item);             // метод преобразования Item в ItemAnswerDto
}
