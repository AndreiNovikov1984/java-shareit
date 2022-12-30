package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingLastNextDto;
import ru.practicum.shareit.booking.dto.TypeStatusDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.TypeStatus;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
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
    private final ItemStorage itemStorage;

    public List<BookingDto> getBookings(long userID, TypeStatusDto status) { // метод получения данных о бронировании
        userService.getUserWithId(userID);
        List<Booking> bookingList;
        switch (status) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerIdOrderByStartDesc(userID);
                break;
            case CURRENT:
                bookingList = bookingRepository.findCurrentByBooker(userID, Timestamp.valueOf(LocalDateTime.now()));
                break;
            case PAST:
                bookingList = bookingRepository.findPastByBooker(userID, Timestamp.valueOf(LocalDateTime.now()));
                break;
            case FUTURE:
                bookingList = bookingRepository.findFutureByBooker(userID, Timestamp.valueOf(LocalDateTime.now()));
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userID, TypeStatus.WAITING);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userID, TypeStatus.REJECTED);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого статуса не существует");
        }
        return bookingList.stream()
                .map(bookingMapper::convertBookingToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getOwnerBookings(long userID, TypeStatusDto status) { // метод получения данных о бронированиях всех вещей пользователя
        userService.getUserWithId(userID);
        List<Booking> bookingList;
        switch (status) {
            case ALL:
                bookingList = bookingRepository.findAllByOwnerOrderByStartDesc(userID);
                break;
            case CURRENT:
                bookingList = bookingRepository.findCurrentByOwner(userID, Timestamp.valueOf(LocalDateTime.now()));
                break;
            case PAST:
                bookingList = bookingRepository.findPastByOwner(userID, Timestamp.valueOf(LocalDateTime.now()));
                break;
            case FUTURE:
                bookingList = bookingRepository.findFutureByOwner(userID, Timestamp.valueOf(LocalDateTime.now()));
                break;
            case WAITING:
                bookingList = bookingRepository.findAllByOwnerAndStatusOrderByStartDesc(userID, TypeStatus.WAITING);
                break;
            case REJECTED:
                bookingList = bookingRepository.findAllByOwnerAndStatusOrderByStartDesc(userID, TypeStatus.REJECTED);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого статуса не существует");
        }
        return bookingList.stream()
                .map(bookingMapper::convertBookingToDto)
                .collect(Collectors.toList());
    }


    public BookingDto getBooking(long userID, long bookingId) { // метод получения данных о бронировании
        checkId(bookingId);
        userService.getUserWithId(userID);
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (!booking.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого бронирования не существует.");
        }
        Item item = getValidItem(booking.get().item.getId());
        if ((userID == item.getOwner()) || (userID == booking.get().booker.getId())) {
            return bookingMapper.convertBookingToDto(booking.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не является владельцем/забронировавшим данную вещь.");
    }

    public BookingDto postBooking(long userId, BookingDto bookingDto) { // метод создания нового запроса на бронирование
        bookingDto.setBooker(userService.getUserWithId(userId));
        Item item = getValidItem(bookingDto.getItemId());
        if (userId == item.getOwner()) {
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

    public BookingDto patchbooking(long userID, String approved, long bookingId) { // метод подтверждения/отклонения запроса на бронирование
        checkId(bookingId);
        userService.getUserWithId(userID);
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (!booking.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такого бронирования не существует.");
        }
        if (!booking.get().status.equals(TypeStatus.WAITING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "По этому бронированию уже дан ответ");
        }
        Item item = getValidItem(booking.get().item.getId());
        if (userID != item.getOwner()) {
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

    public boolean validateBookingOfCommentors(Long itemId, long userID) {
        Optional<Booking> booking = bookingRepository.findBokingByItemAndOwnerValidate(itemId, userID, Timestamp.valueOf(LocalDateTime.now()));
        if (booking.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    private void checkId(long id) {
        if ((id < 0) || (id == 0)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Некорректный id. Попробуйте еще раз.");
        }
    }

    private Item getValidItem(long itemId) {
        Optional<Item> item = itemStorage.findItemById(itemId);
        if (!item.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Такой вещи не существует.");
        }
        return item.get();
    }
}
