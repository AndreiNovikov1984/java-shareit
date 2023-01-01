package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    public long id;
    public String text;
    public long itemId;
    public long authorId;
    public String authorName;
    public LocalDateTime created;
}

