package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder(access = AccessLevel.PUBLIC)
public class ItemRequestDto {
    public long id;
    public String description;
    public LocalDateTime created;

}
