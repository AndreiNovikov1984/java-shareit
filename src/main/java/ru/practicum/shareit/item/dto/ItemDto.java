package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder(access = AccessLevel.PUBLIC)
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long request;
    private List<String> itemFeedback;
}
