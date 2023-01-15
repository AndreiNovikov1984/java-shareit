package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemAnswerDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(access = AccessLevel.PUBLIC)
public class ItemRequestAnswerDto {
        public long id;
        public String description;
        public LocalDateTime created;
        public List<ItemAnswerDto> items;
    }

