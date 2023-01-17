package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.support.Validation;
import ru.practicum.shareit.user.UserRepository;
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
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookingService bookingService;

    public Collection<ItemDto> getItems(Long userId, Integer from, Integer size) {        // метод получения списка вещей по ID пользователя
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of((from / size), size, sortById);
        return itemRepository.findAllByOwnerId(userId, page).stream()
                .map(itemMapper::convertItemToDto)
                .map(this::getLastAndNextBooking)
                .map(this::getCommentForItem)
                .collect(Collectors.toList());
    }

    public ItemDto getItem(Long userId, Long itemId) {                               // метод получения вещи по ID
        Optional<Item> item = itemRepository.findById(itemId);
        if (!item.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id вещи. Попробуйте еще раз.");
        } else {
            ItemDto itemDto = itemMapper.convertItemToDto(item.get());
            itemDto = getCommentForItem(itemDto);
            if (userId == item.get().getOwner().getId()) {
                return getLastAndNextBooking(itemDto);
            } else {
                return itemDto;
            }
        }
    }

    public Collection<ItemDto> search(String text, Integer from, Integer size) {                            // метод поиска вещи
        if ((text == null) || (text.equals(""))) {
            return new ArrayList<>();
        } else {
            Sort sortById = Sort.by(Sort.Direction.ASC, "id");
            Pageable page = PageRequest.of((from / size), size, sortById);
            return itemRepository.searchItem(text, page).stream()
                    .map(itemMapper::convertItemToDto)
                    .collect(Collectors.toList());
        }
    }

    public ItemDto postItem(Long userID, ItemDto itemDto) {         // метод добавления вещи
        Optional<User> user = userRepository.findById(userID);
        if (user.isPresent()) {
            Item item = itemMapper.convertDtoToItem(itemDto);
            item.setOwner(user.get());
            Validation.validationItem(item);
            return itemMapper.convertItemToDto(itemRepository.save(item));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id. Попробуйте еще раз.");
        }
    }

    public ItemDto patchItem(Long userID, Long itemId, ItemDto itemDto) { // метод обновления вещи
        Optional<User> user = userRepository.findById(userID);
        if (user.isPresent()) {
            itemDto.setId(itemId);
            Item item = itemMapper.convertDtoToItem(itemDto);
            item.setOwner(user.get());
            Item itemToUpdate = itemRepository.findById(itemId).get();
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
            return itemMapper.convertItemToDto(itemRepository.save(item));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id. Попробуйте еще раз.");
        }
    }

    public CommentDto postComment(Long userID, Long itemId, Comment comment) {
        Validation.validationComment(comment);
        if (!bookingService.validateBookingOfCommentors(itemId, userID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Нельзя добавить комментарий к данной вещи");
        }
        comment.setAuthor(userRepository.findById(userID).get());
        comment.setItem(itemRepository.findById(itemId).get());
        comment.setCreated(LocalDateTime.now());
        return commentMapper.convertCommentToDto(commentRepository.save(comment));
    }

    private ItemDto getLastAndNextBooking(ItemDto itemDto) {
        itemDto.setLastBooking(bookingService.findLastByItem(itemDto));
        itemDto.setNextBooking(bookingService.findNextByItem(itemDto));
        return itemDto;
    }

    private ItemDto getCommentForItem(ItemDto itemDto) {
        List<Comment> comment = commentRepository.findByItemId(itemDto.getId());
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
        commentDto.setAuthorName(userRepository.findById(commentDto.getAuthorId()).get().getName());
        return commentDto;
    }
}
