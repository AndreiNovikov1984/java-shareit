package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingLastNextDto;
import ru.practicum.shareit.booking.dto.TypeStatusDto;
import ru.practicum.shareit.booking.model.TypeStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.support.Validation;
import ru.practicum.shareit.user.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final ItemRepository itemStorage;

    public List<BookingDto> getBookings(Long userID, TypeStatusDto status, Integer from, Integer size) { // метод получения данных о бронировании
        userService.getUserWithId(userID);
        Page<Booking> bookingList;
        Sort sortById = Sort.by(Sort.Direction.ASC, "start");
        Pageable page = PageRequest.of((from / size), size, sortById);
        switch (status) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerIdOrderByStartDesc(userID, page);
                break;
            case CURRENT:
                bookingList = bookingRepository.findCurrentByBooker(userID, Timestamp.valueOf(LocalDateTime.now()), page);
                break;
            case PAST:
                bookingList = bookingRepository.findPastByBooker(userID, Timestamp.valueOf(LocalDateTime.now()), page);
                break;
            case FUTURE:
                bookingList = bookingRepository.findFutureByBooker(userID, Timestamp.valueOf(LocalDateTime.now()), page);
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userID, TypeStatus.WAITING, page);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userID, TypeStatus.REJECTED, page);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого статуса не существует");
        }
        return bookingList.stream()
                .map(bookingMapper::convertBookingToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getOwnerBookings(Long userID, TypeStatusDto status, Integer from, Integer size) { // метод получения данных о бронированиях всех вещей пользователя
        userService.getUserWithId(userID);
        Page<Booking> bookingList;
        Sort sortBy = Sort.by(Sort.Direction.ASC, "start");
        Pageable page = PageRequest.of((from / size), size, sortBy);
        switch (status) {
            case ALL:
                bookingList = bookingRepository.findAllByOwnerOrderByStartDesc(userID, page);
                break;
            case CURRENT:
                bookingList = bookingRepository.findCurrentByOwner(userID, Timestamp.valueOf(LocalDateTime.now()), page);
                break;
            case PAST:
                bookingList = bookingRepository.findPastByOwner(userID, Timestamp.valueOf(LocalDateTime.now()), page);
                break;
            case FUTURE:
                bookingList = bookingRepository.findFutureByOwner(userID, Timestamp.valueOf(LocalDateTime.now()), page);
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByOwnerAndStatusOrderByStartDesc(userID, TypeStatus.WAITING, page);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByOwnerAndStatusOrderByStartDesc(userID, TypeStatus.REJECTED, page);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого статуса не существует");
        }
        return bookingList.stream()
                .map(bookingMapper::convertBookingToDto)
                .collect(Collectors.toList());
    }


    public BookingDto getBooking(Long userID, Long bookingId) { // метод получения данных о бронировании
        userService.getUserWithId(userID);
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (!booking.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого бронирования не существует.");
        }
        Item item = getValidItem(booking.get().item.getId());
        if ((userID == item.getOwner().getId()) || (userID == booking.get().booker.getId())) {
            return bookingMapper.convertBookingToDto(booking.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не является владельцем/забронировавшим данную вещь.");
    }

    public BookingDto postBooking(Long userId, BookingDto bookingDto) { // метод создания нового запроса на бронирование
        bookingDto.setBooker(userService.getUserWithId(userId));
        Item item = getValidItem(bookingDto.getItemId());
        if (userId == item.getOwner().getId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не может бронировать свою вещь");
        }
        Validation.validationBooking(bookingDto);
        if (item.getAvailable()) {
            bookingDto.setStatus(TypeStatusDto.WAITING);
            Booking booking = bookingMapper.convertDtoToBooking(bookingDto);
            booking.setItem(item);
            return bookingMapper.convertBookingToDto(bookingRepository.save(booking));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Вещь не доступна для бронирования");
        }
    }

    public BookingDto patchbooking(Long userID, String approved, Long bookingId) { // метод подтверждения/отклонения запроса на бронирование
        userService.getUserWithId(userID);
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (!booking.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого бронирования не существует.");
        }
        if (!booking.get().status.equals(TypeStatus.WAITING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "По этому бронированию уже дан ответ");
        }
        Item item = getValidItem(booking.get().item.getId());
        if (userID != item.getOwner().getId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не является владельцем данной вещи.");
        }
        if (approved.equals("true")) {
            booking.get().setStatus(TypeStatus.APPROVED);
        } else if (approved.equals("false")) {
            booking.get().setStatus(TypeStatus.REJECTED);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка в указании статуса бронирования, попробуйте еще раз");
        }
        bookingRepository.save(booking.get());
        return bookingMapper.convertBookingToDto(booking.get());
    }

    public BookingLastNextDto findLastByItem(ItemDto itemDto) {
        Optional<Booking> booking = bookingRepository.findLastByItem(itemDto.getId(), Timestamp.valueOf(LocalDateTime.now()));
        if (booking.isPresent()) {
            return bookingMapper.convertBookingToLNDto(booking.get());
        } else {
            return null;
        }
    }

    public BookingLastNextDto findNextByItem(ItemDto itemDto) {
        Optional<Booking> booking = bookingRepository.findNextByItem(itemDto.getId(), Timestamp.valueOf(LocalDateTime.now()));
        if (booking.isPresent()) {
            return bookingMapper.convertBookingToLNDto(booking.get());
        } else {
            return null;
        }
    }

    public boolean validateBookingOfCommentors(Long itemId, Long userID) {
        Optional<Booking> booking = bookingRepository.findBokingByItemAndOwnerValidate(itemId, userID, Timestamp.valueOf(LocalDateTime.now()));
        return booking.isPresent();
    }

    private Item getValidItem(Long itemId) {
        Optional<Item> item = itemStorage.findById(itemId);
        if (!item.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такой вещи не существует.");
        }
        return item.get();
    }
}
