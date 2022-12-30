package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.support.Validation;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;
    private final UserStorage userStorage;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookingService bookingService;

    public Collection<ItemDto> getItems(long userId) {        // метод получения списка вещей по ID пользователя
        checkId(userId);
        return itemStorage.findAllByOwner(userId).stream()
                .map(itemMapper::convertItemToDto)
                .map(this::getLastAndNextBooking)
                .map(this::getCommentForItem)
                .collect(Collectors.toList());
    }

    public ItemDto getItem(long userId, long itemId) {                               // метод получения вещи по ID
        checkId(userId);
        checkId(itemId);
        Optional<Item> item = itemStorage.findById(itemId);
        if (!item.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id вещи. Попробуйте еще раз.");
        } else {
            ItemDto itemDto = itemMapper.convertItemToDto(item.get());
            itemDto = getCommentForItem(itemDto);
            if (userId == item.get().getOwner()) {
                return getLastAndNextBooking(itemDto);
            } else {
                return itemDto;
            }
        }
    }

    public Collection<ItemDto> search(String text) {                            // метод поиска вещи
        if ((text == null) || (text.equals(""))) {
            return new ArrayList<>();
        } else {
            return itemStorage.searchItem(text).stream()
                    .map(itemMapper::convertItemToDto)
                    .collect(Collectors.toList());
        }
    }

    public ItemDto postItem(long userID, ItemDto itemDto) {         // метод добавления вещи
        checkId(userID);
        Optional<User> user = userStorage.findById(userID);
        if (user.isPresent()) {
            Item item = itemMapper.convertDtoToItem(userID, itemDto);
            Validation.validationItem(item);
            return itemMapper.convertItemToDto(itemStorage.save(item));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id. Попробуйте еще раз.");
        }
    }

    public ItemDto patchItem(long userID, long itemId, ItemDto itemDto) { // метод обновления вещи
        checkId(userID);
        Optional<User> user = userStorage.findById(userID);
        if (user.isPresent()) {
            checkId(itemId);
            itemDto.setId(itemId);
            Item item = itemMapper.convertDtoToItem(userID, itemDto);
            Item itemToUpdate = itemStorage.findItemById(itemId).get();
            if (item.getOwner() != itemToUpdate.getOwner()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не является владельцем данной вещи.");
            }
            if (item.getName() == null) {
                item.setName(itemToUpdate.getName());
            }
            if (item.getDescription() == null) {
                item.setDescription(itemToUpdate.getDescription());
            }
            if (item.getAvailable() == null) {
                item.setAvailable(itemToUpdate.getAvailable());
            }
            if (item.getComments() == null) {
                item.setComments(itemToUpdate.getComments());
            }
            Validation.validationItem(item);
            return itemMapper.convertItemToDto(itemStorage.save(item));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id. Попробуйте еще раз.");
        }
    }

    public CommentDto postComment(long userID, Long itemId, Comment comment) {
        checkId(userID);
        checkId(itemId);
        Validation.validationComment(comment);
        if (!bookingService.validateBookingOfCommentors(itemId, userID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нельзя добавить комментарий к данной вещи");
        }
        comment.setAuthor(userStorage.findUserById(userID).get());
        comment.setItem(itemId);
        comment.setCreated(LocalDateTime.now());
        return commentMapper.convertCommentToDto(commentRepository.save(comment));
    }

    private ItemDto getLastAndNextBooking(ItemDto itemDto) {
        itemDto.setLastBooking(bookingService.findLastByItem(itemDto));
        itemDto.setNextBooking(bookingService.findNextByItem(itemDto));
        return itemDto;
    }

    private ItemDto getCommentForItem(ItemDto itemDto) {
        List<Comment> comment = commentRepository.findByItem(itemDto.getId());
        List<CommentDto> commentUpd;
        if (comment.size() != 0) {
            commentUpd = comment.stream()
                    .map(commentMapper::convertCommentToDto)
                    .map(this::getAuthorNameToComment)
                    .collect(Collectors.toList());
        } else {
            commentUpd = new ArrayList<>();
        }
        itemDto.setComments(commentUpd);
        return itemDto;
    }

    private CommentDto getAuthorNameToComment(CommentDto commentDto) {
        commentDto.setAuthorName(userStorage.findUserById(commentDto.getAuthorId()).get().getName());
        return commentDto;
    }


    private void checkId(long id) {
        if ((id < 0) || (id == 0)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id. Попробуйте еще раз.");
        }
    }
}
