package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingLastNextDto {
    public long id;
    public LocalDateTime start;
    public LocalDateTime end;
    public long itemId;
    public ItemDto item;
    public long bookerId;
    public TypeStatusDto status;
}
