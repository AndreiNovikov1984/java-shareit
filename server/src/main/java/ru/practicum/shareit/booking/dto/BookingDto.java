package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDto {
    public long id;
    public LocalDateTime start;
    public LocalDateTime end;
    public long itemId;
    public ItemDto item;
    public UserDto booker;
    public TypeStatusDto status;
}
