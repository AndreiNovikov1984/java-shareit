package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDtoGate {
    public long id;
    public String text;
    public long itemId;
    public long authorId;
    public String authorName;
    public LocalDateTime created;
}

