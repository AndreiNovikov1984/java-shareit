package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    ItemRequestDto convertItemRequestToDto(ItemRequest itemRequest);             // метод преобразования ItemRequest в ItemRequestDto

    @Mapping(target = "requestor", ignore = true)
    ItemRequest convertDtoToItemRequest(ItemRequestDto itemRequestDto);        // метод преобразования ItemRequestDto в ItemRequest

    ItemRequestAnswerDto convertItemRequestToAnswerDto(ItemRequest itemRequest);             // метод преобразования ItemRequest в ItemRequestAnswerDto

    @Mapping(target = "requestor", ignore = true)
    ItemRequest convertAnswerDtoToItemRequest(ItemRequestAnswerDto itemRequestDto);        // метод преобразования ItemRequestAnswerDto в ItemRequest
}

