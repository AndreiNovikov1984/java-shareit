package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.support.Validation;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final UserRepository userRepository;

    public Collection<ItemRequestAnswerDto> getRequests(long userId) {        // метод получения списка запросов по ID пользователя
        checkId(userId);
        userService.getUserWithId(userId);
        return itemRequestRepository.findAllByRequestorId(userId).stream()
                .map(itemRequestMapper::convertItemRequestToAnswerDto)
                .map(this::getItemsForRequest)
                .collect(Collectors.toList());
    }

    public ItemRequestAnswerDto getRequest(long userId, long requestId) {        // метод получения списка запросов по ID пользователя
        checkId(userId);
        userService.getUserWithId(userId);
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);
        if (!itemRequest.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id запроса вещи. Попробуйте еще раз.");
        }
        ItemRequestAnswerDto itemRequestAnswerDto = itemRequestMapper.convertItemRequestToAnswerDto(itemRequest.get());
        getItemsForRequest(itemRequestAnswerDto);
        return itemRequestAnswerDto;
    }


    public Collection<ItemRequestAnswerDto> getAllRequests(long userId, int from, int size) {        // метод получения списка запросов по ID пользователя
        checkId(userId);
        checkPage(from, size);
        Sort sortById = Sort.by(Sort.Direction.ASC, "created");
        Pageable page = PageRequest.of((from / size), size, sortById);
        Page<ItemRequest> itemRequestsPage = itemRequestRepository.findAllByIdNot(userId, page);
        if (itemRequestsPage.isEmpty()) {
            return new ArrayList<ItemRequestAnswerDto>();
        } else
            return itemRequestsPage.getContent().stream()
                    .map(itemRequestMapper::convertItemRequestToAnswerDto)
                    .map(this::getItemsForRequest)
                    .collect(Collectors.toList());
    }


    public ItemRequestDto postRequest(long userID, ItemRequestDto itemRequestDto) {   // метод добавления запроса вещи
        checkId(userID);
        Validation.validationItemRequest(itemRequestDto);
        Optional<User> user = userRepository.findById(userID);
        if (user.isPresent()) {
            ItemRequest itemRequest = itemRequestMapper.convertDtoToItemRequest(itemRequestDto);
            itemRequest.setRequestor(user.get());
            itemRequest.setCreated(LocalDateTime.now());
            return itemRequestMapper.convertItemRequestToDto(itemRequestRepository.save(itemRequest));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id. Попробуйте еще раз.");
        }
    }

    private ItemRequestAnswerDto getItemsForRequest(ItemRequestAnswerDto itemRequestAnswerDto) {
        List<ItemAnswerDto> items = itemRepository.findAllByRequestId(itemRequestAnswerDto.getId()).stream()
                .map(itemMapper::convertItemToAnswerDto)
                .collect(Collectors.toList());
        itemRequestAnswerDto.setItems(items);
        return itemRequestAnswerDto;
    }

    private void checkId(long id) {
        if ((id < 0) || (id == 0)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id. Попробуйте еще раз.");
        }
    }

    private void checkPage(int from, int size) {
        if ((from < 0) || (size < 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный индекс пагинации. Попробуйте еще раз.");
        }
    }

}
