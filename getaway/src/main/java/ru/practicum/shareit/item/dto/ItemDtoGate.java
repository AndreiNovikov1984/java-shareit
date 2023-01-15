package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingLastNextDtoGate;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder(access = AccessLevel.PUBLIC)
public class ItemDtoGate {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long requestId;
    private List<CommentDtoGate> comments;
    private BookingLastNextDtoGate lastBooking;
    private BookingLastNextDtoGate nextBooking;
}
