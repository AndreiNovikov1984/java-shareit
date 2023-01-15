package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface CommentMapper {
    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "item.id", target = "itemId")
    CommentDto convertCommentToDto(Comment comment);             // метод преобразования Comment в CommentDto

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "itemId", target = "item.id")
    Comment convertDtoToComment(CommentDto commentDto);             // метод преобразования CommentDto в Comment

}
