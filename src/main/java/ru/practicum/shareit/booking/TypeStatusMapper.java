package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import ru.practicum.shareit.booking.dto.TypeStatusDto;
import ru.practicum.shareit.booking.model.TypeStatus;

@Mapper(componentModel = "spring")
public interface TypeStatusMapper {

    TypeStatusDto convertTypeStatusToDto(TypeStatus booking);             // метод преобразования TypeStatus в TypeStatusDto

    @ValueMapping(source = "CURRENT", target = "APPROVED")
    @ValueMapping(source = "PAST", target = "APPROVED")
    @ValueMapping(source = "FUTURE", target = "APPROVED")
    TypeStatus convertDtoToTypeStatus(TypeStatusDto bookingDto);             // метод преобразования TypeStatusDto в TypeStatus
}
