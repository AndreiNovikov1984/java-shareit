package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    public long id;
    public String name;
    public String description;
    public Boolean available;
    public long itemOwnerId;
    public long request;
    public List<String> itemFeedback;
}
