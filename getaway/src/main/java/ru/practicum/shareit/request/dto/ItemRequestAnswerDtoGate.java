package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemAnswerDtoGate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(access = AccessLevel.PUBLIC)
public class ItemRequestAnswerDtoGate {
        public long id;
        public String description;
        public LocalDateTime created;
        public List<ItemAnswerDtoGate> items;
    }

